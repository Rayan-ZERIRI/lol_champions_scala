import scala.util.{Try, Success, Failure}

object DataValidator {
  private def isValid(champion : Champion) : Boolean = {
    champion.role.nonEmpty && champion.tier.nonEmpty && champion.archetype.nonEmpty
  }
  def filterInvalid(champions : List[Champion]): List[Champion] = {
    champions.filter(isValid)
  }
}
