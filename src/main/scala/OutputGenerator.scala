import io.circe._
import io.circe.syntax._
import io.circe.generic.auto._

object OutputGenerator {

  // ========== CASE CLASSES POUR JSON ==========
  
  case class Statistics(
    total_champions_parsed: Int,
    total_champions_valid: Int,
    parsing_errors: Int,
    duplicates_removed: Int
  )

  case class ChampionSummary(
    name: String,
    role: String,
    winRate: Double,
    tier: String
  )

  case class ChampionWithKDA(
    name: String,
    role: String,
    winRate: Double,
    tier: String,
    kda: Double,
    kills: Double,
    deaths: Double,
    assists: Double
  )

  case class ChampionDifficulty(
    name: String,
    difficulty: Int
  )

  case class Results(
    statistics: Statistics,
    champions_by_role: Map[String, Int],
    champions_by_tier: Map[String, Int],
    champions_by_class: Map[String, Int],
    highest_win_rate: List[ChampionSummary],
    most_picked: List[ChampionSummary],
    most_banned: List[ChampionSummary],
    best_kda: List[ChampionWithKDA],
    average_win_rate_by_role: Map[String, Double],
    average_difficulty_by_role: Map[String, Double],
    most_popular_class: String,
    hardest_champions: List[ChampionDifficulty]
  )

  // ========== GÉNÉRATION CONSOLE ==========
  
  def printStats(champions: List[Champion]): Unit = {
    println(s"Champions chargés: ${champions.size}")
    println()

    printDistributions(champions)
    printTopStats(champions)
    printAverages(champions)
    printMiscStats(champions)
  }

  private def printDistributions(champions: List[Champion]): Unit = {
    println("Distribution des roles : ")
    ChampionStats.roleDistribution(champions).foreach { stat =>
      println(s"  ${stat._1} : ${stat._2}")
    }
    println()

    println("Distribution des tiers : ")
    ChampionStats.tierDistribution(champions).foreach { stat =>
      println(s"  ${stat._1} : ${stat._2}")
    }
    println()

    println("Distribution des archetypes : ")
    ChampionStats.archetypeDistribution(champions).foreach { archetype =>
      println(s"  ${archetype._1} : ${archetype._2}")
    }
    println()
  }

  private def printTopStats(champions: List[Champion]): Unit = {
    println("Top 10 WinRate :")
    ChampionStats.topWinRate(champions).zipWithIndex.foreach { case (champion, idx) =>
      println(s"  ${idx + 1}. ${champion.name} : ${champion.winRate}% (Tier ${champion.tier})")
    }
    println()

    println("Top 10 PickRate :")
    ChampionStats.topPickRate(champions).zipWithIndex.foreach { case (champion, idx) =>
      println(s"  ${idx + 1}. ${champion.name} : ${champion.pickRate}%")
    }
    println()

    println("Top 10 BanRate :")
    ChampionStats.topBanRate(champions).zipWithIndex.foreach { case (champion, idx) =>
      println(s"  ${idx + 1}. ${champion.name} : ${champion.banRate}%")
    }
    println()

    println("Top 10 KDA :")
    ChampionStats.topKDA(champions).zipWithIndex.foreach { case (champion, idx) =>
      println(s"  ${idx + 1}. ${champion.name} : ${champion.calculateKDA} KDA (${champion.avgKills}/${champion.avgDeaths}/${champion.avgAssists})")
    }
    println()
  }

  private def printAverages(champions: List[Champion]): Unit = {
    println("Winrate par role : ")
    ChampionStats.avgRoleWinrate(champions).foreach { stat =>
      println(s"  ${stat._1} : ${stat._2} %")
    }
    println()

    println("Difficulte par role : ")
    ChampionStats.avgRoleDiff(champions).foreach { stat =>
      println(s"  ${stat._1} : ${stat._2}/10")
    }
    println()

    println("Or par role : ")
    ChampionStats.avgRoleGold(champions).foreach { stat =>
      println(s"  ${stat._1} : ${stat._2} or")
    }
    println()

    println("Degats par role : ")
    ChampionStats.avgRoleDamage(champions).foreach { stat =>
      println(s"  ${stat._1} : ${stat._2} degats")
    }
    println()
  }

  private def printMiscStats(champions: List[Champion]): Unit = {
    println("Top 10 champions les plus durs :")
    ChampionStats.topDifficulty(champions).zipWithIndex.foreach { case (champion, idx) =>
      println(s"  ${idx + 1}. ${champion.name} : ${champion.difficulty}/10")
    }
    println()

    println("Classe la plus populaire :")
    val mostPopular = ChampionStats.mostPopularArchetype(champions)
    println(s"  ${mostPopular._1} : ${mostPopular._2} champions (${mostPopular._2}/${champions.size})")
  }

  // ========== GÉNÉRATION RESULTS.JSON ==========
  
  def generateResultsJson(processResult: DataManager.ProcessResult, champions: List[Champion]): Json = {
    val stats = Statistics(
      total_champions_parsed = processResult.totalEntries,
      total_champions_valid = processResult.validatedCount,
      parsing_errors = processResult.parsingErrors,
      duplicates_removed = processResult.duplicatesRemoved
    )

    val topWinRate = ChampionStats.topWinRate(champions).map(c =>
      ChampionSummary(c.name, c.role, c.winRate, c.tier)
    )

    val topPicked = ChampionStats.topPickRate(champions).map(c =>
      ChampionSummary(c.name, c.role, c.pickRate, c.tier)
    )

    val topBanned = ChampionStats.topBanRate(champions).map(c =>
      ChampionSummary(c.name, c.role, c.banRate, c.tier)
    )

    val topKDA = ChampionStats.topKDA(champions).map(c =>
      ChampionWithKDA(c.name, c.role, c.winRate, c.tier, c.calculateKDA, c.avgKills, c.avgDeaths, c.avgAssists)
    )

    val hardest = ChampionStats.topDifficulty(champions).map(c =>
      ChampionDifficulty(c.name, c.difficulty)
    )

    val mostPopular = ChampionStats.mostPopularArchetype(champions)._1

    val results = Results(
      statistics = stats,
      champions_by_role = ChampionStats.roleDistribution(champions),
      champions_by_tier = ChampionStats.tierDistribution(champions),
      champions_by_class = ChampionStats.archetypeDistribution(champions),
      highest_win_rate = topWinRate,
      most_picked = topPicked,
      most_banned = topBanned,
      best_kda = topKDA,
      average_win_rate_by_role = ChampionStats.avgRoleWinrate(champions),
      average_difficulty_by_role = ChampionStats.avgRoleDiff(champions),
      most_popular_class = mostPopular,
      hardest_champions = hardest
    )

    results.asJson
  }

  // ========== GÉNÉRATION REPORT.TXT ==========
  
  def generateTextReport(processResult: DataManager.ProcessResult, champions: List[Champion], duration: Double): String = {
    val sb = new StringBuilder

    sb.append("===============================================\n")
    sb.append("  RAPPORT D'ANALYSE - LEAGUE OF LEGENDS\n")
    sb.append("===============================================\n\n")

    // Parsing stats
    sb.append("STATISTIQUES DE PARSING\n")
    sb.append("---------------------------\n")
    sb.append(f"- Entrées totales lues      : ${processResult.totalEntries}%d\n")
    sb.append(f"- Entrées valides           : ${processResult.validatedCount}%d\n")
    sb.append(f"- Erreurs de parsing        : ${processResult.parsingErrors}%d\n")
    sb.append(f"- Doublons supprimés        : ${processResult.duplicatesRemoved}%d\n\n")

    // Role distribution
    sb.append("⚔️  RÉPARTITION PAR RÔLE\n")
    sb.append("------------------------\n")
    val roleStats = ChampionStats.roleDistribution(champions).toList.sortBy(-_._2)
    roleStats.foreach { case (role, count) =>
      sb.append(f"- $role%-24s : $count%d champions\n")
    }
    sb.append("\n")

    // Tier distribution
    sb.append("🏆 RÉPARTITION PAR TIER\n")
    sb.append("------------------------\n")
    val tierOrder = List("S", "A", "B", "C", "D")
    val tierStats = ChampionStats.tierDistribution(champions)
    val tierLabels = Map("S" -> "Excellent", "A" -> "Très bon", "B" -> "Bon", "C" -> "Moyen", "D" -> "Faible")
    tierOrder.foreach { tier =>
      val count = tierStats.getOrElse(tier, 0)
      val label = tierLabels(tier)
      sb.append(f"- $tier ($label%-9s)             : $count%d champions\n")
    }
    sb.append("\n")

    // Class distribution
    sb.append("RÉPARTITION PAR CLASSE\n")
    sb.append("--------------------------\n")
    val classStats = ChampionStats.archetypeDistribution(champions).toList.sortBy(-_._2)
    classStats.foreach { case (archetype, count) =>
      sb.append(f"- $archetype%-24s : $count%d champions\n")
    }
    sb.append("\n")

    // Top 10 Win Rate
    sb.append("TOP 10 - MEILLEUR WIN RATE\n")
    sb.append("------------------------------\n")
    ChampionStats.topWinRate(champions).take(10).zipWithIndex.foreach { case (c, idx) =>
      sb.append(f"${idx + 1}%2d. ${c.name}%-22s : ${c.winRate}%.2f%% (Tier ${c.tier})\n")
    }
    sb.append("\n")

    // Top 10 Pick Rate
    sb.append("TOP 10 - PICK RATE\n")
    sb.append("----------------------\n")
    ChampionStats.topPickRate(champions).take(10).zipWithIndex.foreach { case (c, idx) =>
      sb.append(f"${idx + 1}%2d. ${c.name}%-22s : ${c.pickRate}%.2f%%\n")
    }
    sb.append("\n")

    // Top 10 Ban Rate
    sb.append("TOP 10 - BAN RATE\n")
    sb.append("---------------------\n")
    ChampionStats.topBanRate(champions).take(10).zipWithIndex.foreach { case (c, idx) =>
      sb.append(f"${idx + 1}%2d. ${c.name}%-22s : ${c.banRate}%.2f%%\n")
    }
    sb.append("\n")

    // Top 10 KDA
    sb.append("TOP 10 - MEILLEUR KDA\n")
    sb.append("-------------------------\n")
    ChampionStats.topKDA(champions).take(10).zipWithIndex.foreach { case (c, idx) =>
      val kda = c.calculateKDA
      sb.append(f"${idx + 1}%2d. ${c.name}%-22s : ${kda}%.2f KDA (${c.avgKills}%.1f/${c.avgDeaths}%.1f/${c.avgAssists}%.1f)\n")
    }
    sb.append("\n")

    // Average stats by role
    sb.append("MOYENNES PAR RÔLE\n")
    sb.append("---------------------\n")
    sb.append("WIN RATE MOYEN :\n")
    val avgWinRate = ChampionStats.avgRoleWinrate(champions).toList.sortBy(-_._2)
    avgWinRate.foreach { case (role, wr) =>
      sb.append(f"- $role%-24s : ${wr}%.2f%%\n")
    }
    sb.append("\n")

    sb.append("DIFFICULTÉ MOYENNE :\n")
    val avgDiff = ChampionStats.avgRoleDiff(champions).toList.sortBy(-_._2)
    avgDiff.foreach { case (role, diff) =>
      sb.append(f"- $role%-24s : ${diff}%.2f/10\n")
    }
    sb.append("\n")

    // Economy by role
    sb.append("ÉCONOMIE MOYENNE PAR RÔLE\n")
    sb.append("-----------------------------\n")
    val avgGold = ChampionStats.avgRoleGold(champions).toList.sortBy(-_._2)
    avgGold.foreach { case (role, gold) =>
      sb.append(f"- $role%-24s : $gold%d or\n")
    }
    sb.append("\n")

    // Damage by role
    sb.append("DÉGÂTS MOYENS PAR RÔLE\n")
    sb.append("--------------------------\n")
    val avgDamage = ChampionStats.avgRoleDamage(champions).toList.sortBy(-_._2)
    avgDamage.foreach { case (role, damage) =>
      sb.append(f"- $role%-24s : $damage%d\n")
    }
    sb.append("\n")

    // Hardest champions
    sb.append("CHAMPIONS LES PLUS DIFFICILES\n")
    sb.append("----------------------------------\n")
    ChampionStats.topDifficulty(champions).take(10).zipWithIndex.foreach { case (c, idx) =>
      sb.append(f"${idx + 1}%2d. ${c.name}%-22s : ${c.difficulty}%d/10\n")
    }
    sb.append("\n")

    // Most popular class
    sb.append(" CLASSE LA PLUS POPULAIRE\n")
    sb.append("----------------------------\n")
    val (mostPopular, count) = ChampionStats.mostPopularArchetype(champions)
    val percentage = (count.toDouble / champions.size) * 100
    sb.append(f"- $mostPopular%-24s : $count%d champions (${percentage}%.1f%%)\n\n")

    // Performance
    sb.append("  PERFORMANCE\n")
    sb.append("---------------\n")
    val entriesPerSec = processResult.totalEntries / duration
    sb.append(f"- Temps de traitement       : $duration%.3f secondes\n")
    sb.append(f"- Entrées/seconde           : ${entriesPerSec}%.0f\n\n")

    sb.append("===============================================\n")

    sb.toString
  }
}
