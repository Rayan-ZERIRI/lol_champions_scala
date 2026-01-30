import io.circe._
import io.circe.parser._
import io.circe.syntax._
import io.circe.generic.auto._
import scala.io.Source
import scala.util.Try
import java.io.PrintWriter

object DataManager {

  // ========== CHARGEMENT ==========
  
  /**
   * Load champions from any JSON file (dirty or clean).
   * Automatically handles parsing errors and returns structured result.
   */
  def loadChampions(filename: String): Either[String, LoadResult] = {
    val jsonString = Try {
      val source = Source.fromFile(filename)
      try source.mkString
      finally source.close()
    }.toEither.left.map(_.getMessage)

    for {
      content <- jsonString
      json    <- parse(content).left.map(_.getMessage)
      arr     <- json.as[List[Json]].left.map(_.getMessage)
    } yield {
      val totalLines = arr.size
      val decoded: List[Either[DecodingFailure, Champion]] = arr.map(_.as[Champion])
      val (invalid, valid) = decoded.partition(_.isLeft)

      LoadResult(
        champions = valid.collect { case Right(c) => c },
        parsingErrors = invalid.size,
        totalEntries = totalLines
      )
    }
  }

  // ========== NETTOYAGE ==========
  
  /**
   * Clean loaded champions: validate and deduplicate.
   */
  def cleanChampions(champions: List[Champion]): CleanResult = {
    // Validation
    val validatedChampions = champions.flatMap(Validator.validate)

    // Deduplication by ID
    val groupedById = validatedChampions.groupBy(_.id)
    val duplicateCount = validatedChampions.size - groupedById.size
    val uniqueChampions = groupedById.values.map(_.head).toList

    CleanResult(
      uniqueChampions = uniqueChampions,
      validatedCount = validatedChampions.size,
      duplicatesRemoved = duplicateCount
    )
  }

  /**
   * Load and clean in one step.
   */
  def loadAndClean(filename: String): Either[String, ProcessResult] = {
    loadChampions(filename).map { loadResult =>
      val cleanResult = cleanChampions(loadResult.champions)
      
      ProcessResult(
        champions = cleanResult.uniqueChampions,
        totalEntries = loadResult.totalEntries,
        parsingErrors = loadResult.parsingErrors,
        validatedCount = cleanResult.validatedCount,
        duplicatesRemoved = cleanResult.duplicatesRemoved
      )
    }
  }

  // ========== SAUVEGARDE ==========
  
  /**
   * Save champions to a JSON file.
   */
  def saveChampions(filename: String, champions: List[Champion]): Either[String, Unit] = {
    Try {
      val json = champions.asJson.spaces2
      val writer = new PrintWriter(filename)
      try writer.write(json)
      finally writer.close()
    }.toEither.left.map(_.getMessage)
  }

  /**
   * Save JSON results to a file.
   */
  def saveJsonResults(filename: String, json: Json): Either[String, Unit] = {
    Try {
      val writer = new PrintWriter(filename)
      try writer.write(json.spaces2)
      finally writer.close()
    }.toEither.left.map(_.getMessage)
  }

  /**
   * Save text report to a file.
   */
  def saveTextReport(filename: String, report: String): Either[String, Unit] = {
    Try {
      val writer = new PrintWriter(filename)
      try writer.write(report)
      finally writer.close()
    }.toEither.left.map(_.getMessage)
  }

  // ========== AFFICHAGE STATS ==========
  
  /**
   * Print processing statistics.
   */
  def printProcessStats(result: ProcessResult): Unit = {
    println(s"Nombre total d'entrées : ${result.totalEntries}")
    println(s"Erreurs de parsing : ${result.parsingErrors}")
    println(s"Total valides : ${result.validatedCount}")
    println(s"Doublons : ${result.duplicatesRemoved}")
    println(s"Champions uniques : ${result.champions.size}")
  }

  // ========== CASE CLASSES ==========
  
  case class LoadResult(
    champions: List[Champion],
    parsingErrors: Int,
    totalEntries: Int
  )

  case class CleanResult(
    uniqueChampions: List[Champion],
    validatedCount: Int,
    duplicatesRemoved: Int
  )

  case class ProcessResult(
    champions: List[Champion],
    totalEntries: Int,
    parsingErrors: Int,
    validatedCount: Int,
    duplicatesRemoved: Int
  )
}
