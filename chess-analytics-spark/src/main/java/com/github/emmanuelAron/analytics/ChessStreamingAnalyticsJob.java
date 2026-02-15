package com.github.emmanuelAron.analytics;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import java.util.List;

import static org.apache.spark.sql.functions.*;

public class ChessStreamingAnalyticsJob {

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession.builder().appName("ChessStreamingAnalytics").master("local[*]").getOrCreate();
        spark.sparkContext().setLogLevel("WARN");
        System.out.println("=== Chess Streaming Analytics Started ===");

        // ===============================
        // MongoDB Configuration
        // ===============================
        spark.conf().set("spark.mongodb.write.connection.uri", "mongodb://localhost:27017/chess");
        // ===============================
        // Schema (Lichess JSONL format)
        // ===============================
        StructType schema = new StructType()
                .add("gameId", DataTypes.StringType)
                .add("moves", DataTypes.StringType)
                .add("rated", DataTypes.BooleanType)
                .add("speed", DataTypes.StringType);
        // ===============================
        // Streaming Source (Folder)
        // ===============================
        Dataset<Row> games = spark.readStream().format("json").schema(schema).option("maxFilesPerTrigger", 1).load("data/input"); // process incrementally

        // ===============================
        // Streaming Logic
        // ===============================
        games.writeStream().foreachBatch((batchDF, batchId) -> {
                    System.out.println("===== Processing Batch " + batchId + " =====");

                    // =====================================================
                    // TOTAL GAMES DELTA
                    // =====================================================
                    long batchGameCount = batchDF.count();

                    Dataset<Row> totalGamesDelta = spark.createDataFrame(
                                    List.of(RowFactory.create("TOTAL_GAMES", batchGameCount)),
                                    new StructType()
                                            .add("_id", DataTypes.StringType)
                                            .add("delta", DataTypes.LongType)
                            )
                            .withColumn("type", lit("TOTAL_GAMES"))
                            .withColumn("updatedAt", current_timestamp());

                    totalGamesDelta.write()
                            .format("mongodb")
                            .mode("append")
                            .option("database", "chess")
                            .option("collection", "global_stats_delta")
                            .save();

                    System.out.println("---- Total Games Delta ----");
                    System.out.println("Batch games: " + batchGameCount);

                    // Split & explode → one row per move
                    Dataset<Row> explodedMoves = batchDF
                            .withColumn("moveArray", split(col("moves"), " "))
                            .withColumn("move", explode(col("moveArray")))
                            .select("gameId", "move")
                            .filter(col("move").isNotNull());
                    // =====================================================
                    // FIRST MOVE DELTA (only first move per game)
                    // =====================================================
                    Dataset<Row> firstMovesDelta  = explodedMoves
                            .groupBy("gameId")
                            .agg(first("move").alias("firstMove"))
                            .groupBy("firstMove")
                            .count()
                            .withColumnRenamed("count", "delta")
                            .withColumn("type", lit("FIRST_MOVE"))
                            .withColumn("updatedAt", current_timestamp());

                    firstMovesDelta.write()
                            .format("mongodb")
                            .mode("append")
                            .option("database", "chess")
                            .option("collection", "first_moves_delta")
                            .save();

                    System.out.println("---- First Move Delta ----");
                    firstMovesDelta.orderBy(desc("delta")).show(10, false);

                    // =====================================================
                    // OPENING PATTERN DELTA (first two half-moves)
                    // =====================================================
                    Dataset<Row> openingPatterns = explodedMoves
                            .groupBy("gameId")
                            .agg(slice(collect_list("move"), 1, 2).alias("openingMoves"))
                            .filter(size(col("openingMoves")).equalTo(2))
                            .withColumn("openingPattern", concat_ws(" ", col("openingMoves")));

                    Dataset<Row> openingPatternDelta  = openingPatterns
                            .groupBy("openingPattern")
                            .count()
                            .withColumnRenamed("count", "delta")
                            .withColumn("type", lit("OPENING_PATTERN"))
                            .withColumn("updatedAt", current_timestamp());

                    openingPatternDelta.write()
                            .format("mongodb")
                            .mode("append")
                            .option("database", "chess")
                            .option("collection", "opening_patterns_delta")
                            .save();

                    System.out.println("---- Opening Pattern Stats ----");
                    openingPatternDelta.orderBy(desc("delta")).show(10, false);

                })
                .option("checkpointLocation", "data/checkpoints")
                .start()
                .awaitTermination();
    }
}