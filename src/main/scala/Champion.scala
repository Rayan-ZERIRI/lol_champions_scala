case class Champion(
  id : Int,
  name : String,
  role: Option[String],
  difficulty: Int,
  releaseYear: Int,
  pickRate: Double,
  banRate: Double,
  winRate: Double,
  gamesPlayed: Int,
  avgKills: Double,
  avgDeaths: Double,
  avgAssists: Double,
  avgGold: Int,
  avgDamage: Int,
  tier: Option[String],
  archetype: Option[String]
)