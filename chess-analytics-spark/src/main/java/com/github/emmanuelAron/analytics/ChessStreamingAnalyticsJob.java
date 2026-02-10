package com.github.emmanuelAron.analytics;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.functions.*;

public class ChessStreamingAnalyticsJob {

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession.builder()
                .appName("ChessStreamingAnalytics")
                .master("local[*]")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        System.out.println("Chess Streaming Analytics Job started");

        // =========================
        // Schema (JSONL Lichess)
        // =========================
        StructType schema = new StructType()
                .add("gameId", DataTypes.StringType)
                .add("moves", DataTypes.StringType)
                .add("rated", DataTypes.BooleanType)
                .add("speed", DataTypes.StringType);

        // =========================
        // Streaming source (FILES)
        // =========================
        Dataset<Row> games = spark.readStream()
                .format("json")
                .schema(schema)
                .load("data/input");

        // =========================
        // Streaming sink
        // =========================
        games.writeStream()
                .foreachBatch((batchDF, batchId) -> {

                    System.out.println("========== Batch " + batchId + " ==========");

                    // Split & explode → one row per move
                    Dataset<Row> explodedMoves = batchDF
                            .withColumn("moveArray", split(col("moves"), " "))
                            .withColumn("move", explode(col("moveArray")))
                            .select("gameId", "move")
                            .filter(col("move").isNotNull());

                    // ==================================================
                    // FIRST MOVE FREQUENCY (White)
                    // ==================================================
                    Dataset<Row> firstMoves = explodedMoves
                            .groupBy("gameId")
                            .agg(first("move").alias("firstMove"))
                            .groupBy("firstMove")
                            .count()
                            .orderBy(desc("count"));

                    System.out.println("---- First move frequency ----");
                    firstMoves.show(10, false);

                    // -------------------------
                    // Opening patterns (first 2 half-moves)
                    // -------------------------
                    Dataset<Row> openingPatterns = explodedMoves
                            .groupBy("gameId")
                            .agg(
                                    slice(collect_list("move"), 1, 2).alias("openingMoves")
                            )
                            .filter(size(col("openingMoves")).equalTo(2))
                            .withColumn("openingPattern", concat_ws(" ", col("openingMoves")));

                    Dataset<Row> openingFrequency = openingPatterns
                            .groupBy("openingPattern")
                            .count()
                            .orderBy(desc("count"));

                    openingFrequency.show(10, false);

                })
                .start()
                .awaitTermination();
    }
}