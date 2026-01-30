object Main {
  def main(args: Array[String]): Unit = {
    val start = System.currentTimeMillis()

    // Fichier d'entrée (peut être dirty, clean ou large)
    val inputFile = if (args.length > 0) args(0) else "data/data_dirty.json"
    val outputDir = "output"
    val cleanFile = "data/data_clean.json"
    val resultsFile = s"$outputDir/results.json"
    val reportFile = s"$outputDir/report.txt"

    // Créer les dossiers nécessaires
    new java.io.File(outputDir).mkdirs()
    new java.io.File("data").mkdirs()

    println(s"=== TRAITEMENT DE : $inputFile ===\n")

    // Charger et nettoyer les données
    DataManager.loadAndClean(inputFile) match {
      case Left(error) =>
        println(s"Erreur de chargement : $error")
        return

      case Right(processResult) =>
        // Afficher les statistiques de traitement
        DataManager.printProcessStats(processResult)
        println()

        // Sauvegarder les champions nettoyés
        DataManager.saveChampions(cleanFile, processResult.champions) match {
          case Right(_) => println(s"✓ Fichier clean sauvegardé: $cleanFile\n")
          case Left(error) => println(s"✗ Erreur de sauvegarde: $error\n")
        }

        // Afficher les statistiques détaillées
        println("=== STATISTIQUES DÉTAILLÉES ===\n")
        OutputGenerator.printStats(processResult.champions)

        // Générer results.json
        val resultsJson = OutputGenerator.generateResultsJson(processResult, processResult.champions)
        DataManager.saveJsonResults(resultsFile, resultsJson) match {
          case Right(_) => println(s"\n✓ Résultats JSON sauvegardés: $resultsFile")
          case Left(error) => println(s"\n✗ Erreur de sauvegarde JSON: $error")
        }

        // Générer report.txt
        val duration = (System.currentTimeMillis() - start) / 1000.0
        val textReport = OutputGenerator.generateTextReport(processResult, processResult.champions, duration)
        DataManager.saveTextReport(reportFile, textReport) match {
          case Right(_) => println(s"✓ Rapport texte sauvegardé: $reportFile")
          case Left(error) => println(s"✗ Erreur de sauvegarde rapport: $error")
        }

        println(f"\nTraitement effectué en $duration%.3f secondes")
    }
  }
}
