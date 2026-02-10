package com.github.emmanuelAron.analytics;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.StructType;

import static org.apache.spark.sql.functions.*;

public class ChessKafkaAnalyticsJob {

    public static void main(String[] args) throws Exception {

        SparkSession spark = SparkSession.builder()
                .appName("ChessKafkaAnalytics")
                .master("local[*]")
                .getOrCreate();

        spark.sparkContext().setLogLevel("WARN");

        System.out.println("Spark Chess Analytics Job started");

        // Schema MOVE_PLAYED
        StructType schema = new StructType()
                .add("gameId", DataTypes.StringType)
                .add("moveNumber", DataTypes.IntegerType)
                .add("move", DataTypes.StringType)
                .add("player", DataTypes.StringType)
                .add("opening", DataTypes.StringType)
                .add("timestamp", DataTypes.LongType);

        // Kafka source
        Dataset<Row> kafkaStream = spark.readStream()
                .format("kafka")
                .option("kafka.bootstrap.servers", "localhost:9092")
                .option("subscribe", "chess.events")
                .option("startingOffsets", "earliest")
                .load();

        Dataset<Row> parsed = kafkaStream
                .selectExpr("CAST(value AS STRING) AS json")
                .select(from_json(col("json"), schema).as("data"))
                .select("data.*");

        Dataset<Row> openingStats = parsed
                .filter(col("opening").isNotNull())
                .groupBy("opening")
                .count()
                .orderBy(desc("count"));

        openingStats.writeStream()
                .outputMode("complete")
                .format("console")
                .option("truncate", false)
                .start()
                .awaitTermination();
    }
}