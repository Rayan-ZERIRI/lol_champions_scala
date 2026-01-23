object Main {
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()
    val filename = "data/data_dirty.json"

    DataLoader.loadChampions(filename) match {
      case Right(champions) =>
        println("Champions chargés :")
        champions.foreach(println)

      case Left(errorMsg) =>
        println(s"Erreur de chargement : $errorMsg")
    }
    val duration = (System.currentTimeMillis() - start) / 1000.0
    println(f"Traitement effectué en $duration%.3f secondes")
  }
}