object Main {
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    val filename = "data/data_clean.json"

    DataLoader.loadChampions(filename) match {
      case Right(champions) =>

        println("Distribution des roles : ")
        ChampionStats.roleDistribution(champions).map(stat => println(stat._1 + " : " + stat._2))

        println("Distribution des tiers : ")
        ChampionStats.tierDistribution(champions).map(stat => println(stat._1 + " : " + stat._2))

        println("Distribution des archetypes : ")
        ChampionStats.archetypeDistribution(champions).map(archetype => println(archetype._1 + " : " + archetype._2))

        println("Top 10 WinRate :")
        ChampionStats.topWinRate(champions).map(champion => println(ChampionStats.topWinRate(champions).indexOf(champion) + 1 + ". " + champion.name + " : " + champion.winRate + "% (Tier " + champion.tier + ")"))

        println("Top 10 PickRate :")
        ChampionStats.topPickRate(champions).map(champion => println(ChampionStats.topPickRate(champions).indexOf(champion) + 1 + ". " + champion.name + " : " + champion.pickRate + "%"))
        
        println("Top 10 BanRate :")
        ChampionStats.topBanRate(champions).map(champion => println(ChampionStats.topBanRate(champions).indexOf(champion) + 1 + ". " + champion.name + " : " + champion.banRate + "%"))
        
        println("Top 10 KDA :")
        ChampionStats.topKDA(champions).map(champion => println(ChampionStats.topKDA(champions).indexOf(champion) + 1 + ". " + champion.name + " : " + champion.calculateKDA + " KDA (" + champion.avgKills + "/" + champion.avgDeaths + "/" + champion.avgAssists +  ")"))

        println("Winrate par role : ")
        ChampionStats.avgRoleWinrate(champions).map(stat => println(stat._1 + " : " + stat._2 + " %"))

        println("Difficulte par role : ")
        ChampionStats.avgRoleDiff(champions).map(stat => println(stat._1 + " : " + stat._2 + "/10"))

        println("Or par role : ")
        ChampionStats.avgRoleGold(champions).map(stat => println(stat._1 + " : " + stat._2 + " or"))

        println("Degats par role : ")
        ChampionStats.avgRoleDamage(champions).map(stat => println(stat._1 + " : " + stat._2 + " degats"))

        println("Top 10 champions les plus durs :")
        ChampionStats.topDifficulty(champions).map(champion => println(ChampionStats.topDifficulty(champions).indexOf(champion) + 1 + ". " + champion.name + " : " + champion.difficulty + "/10"))

        println("Classe la plus populaire :")
        println(ChampionStats.mostPopularArchetype(champions)._1 + " : " + ChampionStats.mostPopularArchetype(champions)._2 + " champions " + ChampionStats.mostPopularArchetype(champions)._2 + "/" + champions.size)

      case Left(errorMsg) =>
        println(s"Erreur de chargement : $errorMsg")
    }
    val duration = (System.currentTimeMillis() - start) / 1000.0
    println(f"Traitement effectuée en $duration%.3f secondes")
  }
}
