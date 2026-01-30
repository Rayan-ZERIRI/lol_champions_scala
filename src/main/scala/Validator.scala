object Validator {
  val VALID_ROLES: Set[String] = Set("Top", "Jungle", "Mid", "ADC", "Support")
  val VALID_TIERS: Set[String] = Set("S", "A", "B", "C", "D")
  val MIN_YEAR = 2009
  val MAX_YEAR = 2025
  val MIN_DIFFICULTY = 1
  val MAX_DIFFICULTY = 10

  def validate(raw: Champion): Option[Champion] = {
    for {
      role <- Some(raw.role).filter(VALID_ROLES.contains)
      tier <- Some(raw.tier).filter(VALID_TIERS.contains)
      archetype <- Some(raw.archetype)
      _ <- validateRates(raw)
      _ <- validateDifficulty(raw.difficulty)
      _ <- validateYear(raw.releaseYear)
      _ <- validateStats(raw)
    } yield raw.copy(role = role, tier = tier, archetype = archetype)
  }

  private def validateRates(raw: Champion): Option[Unit] =
    if (raw.pickRate >= 0 && raw.pickRate <= 100 &&
      raw.banRate  >= 0 && raw.banRate  <= 100 &&
      raw.winRate  >= 0 && raw.winRate  <= 100) Some(()) else None

  private def validateDifficulty(difficulty: Int): Option[Unit] =
    if (difficulty >= MIN_DIFFICULTY && difficulty <= MAX_DIFFICULTY) Some(()) else None

  private def validateYear(year: Int): Option[Unit] =
    if (year >= MIN_YEAR && year <= MAX_YEAR) Some(()) else None

  private def validateStats(raw: Champion): Option[Unit] =
    if (raw.avgKills >= 0 && raw.avgDeaths >= 0 && raw.avgAssists >= 0) Some(()) else None
}