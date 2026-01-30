import scala.util.{Try, Success, Failure}

object ChampionStats {
  // Répartition par rôle, tier et archetype
  def roleDistribution(champions : List[Champion]) : Map[String, Int] = {
    champions.groupBy(champion => champion.role)
      .map(role => (role._1, role._2.size))
  }

  def tierDistribution(champions: List[Champion]): Map[String, Int] = {
    champions.groupBy(champion => champion.tier)
      .map(tier => (tier._1, tier._2.size))
  }

  def archetypeDistribution(champions: List[Champion]): Map[String, Int] = {
    champions.groupBy(champion => champion.archetype)
      .map(archetype => (archetype._1, archetype._2.size))
  }

  // Top 10 par pick rate, win rate, ban rate et meilleur KDA
  def topWinRate(champions : List[Champion], n: Int=10) : List[Champion] = {
    champions.sortBy(-_.winRate)
      .take(n)
  }

  def topPickRate(champions: List[Champion], n: Int=10): List[Champion] = {
    champions.sortBy(-_.pickRate)
      .take(n)
  }

  def topBanRate(champions: List[Champion], n: Int=10): List[Champion] = {
    champions.sortBy(-_.banRate)
      .take(n)
  }

  def topKDA(champions: List[Champion], n: Int=10) : List[Champion] = {
    champions.sortBy(-_.calculateKDA)
      .take(n)
  }

  // Moyenne par rôle : winrate, difficulté et économie
  def avgRoleWinrate(champions: List[Champion]) : Map[String, Double]= {
    champions.groupBy(champion => champion.role)
      .mapValues(group => group.map(_.winRate).sum / group.size.toDouble)
      .toMap
  }

  def avgRoleDiff(champions: List[Champion]): Map[String, Double] = {
    champions.groupBy(champion => champion.role)
      .mapValues(group => group.map(_.difficulty).sum / group.size.toDouble)
      .toMap
  }

  def avgRoleGold(champions: List[Champion]): Map[String, Int] = {
    champions.groupBy(champion => champion.role)
      .mapValues(group => group.map(_.avgGold).sum / group.size)
      .toMap
  }

  def avgRoleDamage(champions: List[Champion]): Map[String, Int] = {
    champions.groupBy(champion => champion.role)
      .mapValues(group => group.map(_.avgDamage).sum / group.size)
      .toMap
  }

  // Champions les plus durs (top 10)
  def topDifficulty(champions: List[Champion], n: Int=10) : List[Champion] = {
    champions.sortBy(-_.difficulty)
      .take(n)
  }

  // Role le plus choisi
  def mostPopularArchetype(champions: List[Champion]): (String, Int)  = {
    champions.groupBy(champion => champion.archetype)
      .mapValues(group => group.size)
      .toList
      .sortBy(-_.size)
      .toMap
      .head
  }
}