import io.circe._

import io.circe._
import io.circe.generic.auto._
import io.circe.parser._
import scala.io.Source
import scala.util.{Try, Success, Failure}

object DataLoader {
  def loadChampions(filename : String): Either[String, List[Champion]] = {
    Try {
      val source = Source.fromFile(filename)
      val content = source.mkString
      source.close()
      content
    } match {
      case Success(jsonString) => decode[List[Champion]](jsonString).left.map(_.getMessage)
      case Failure(error) => Left(error.getMessage)
    }
  }
}
