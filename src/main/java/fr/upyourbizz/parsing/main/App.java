package fr.upyourbizz.parsing.main;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import fr.upyourbizz.core.Contexte;
import fr.upyourbizz.parsing.magento.Parser;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) {
        try {
            String executionPath = System.getProperty("user.dir");
            System.out.print("Executing at =>" + executionPath.replace("\\", "/"));
        }
        catch (Exception e) {
            System.out.println("Exception caught =" + e.getMessage());
        }

        // create and configure beans
        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");

        Contexte contexte = context.getBean("contexte", Contexte.class);
        contexte.setFromDisk(true);
        contexte.setSaveFile(true);
        contexte.setHomePagePath("www.terredeson.com");

        // retrieve configured instance
        Parser parser = context.getBean("parser", Parser.class);
        parser.extractProductUrlFromDisk("www.terredeson.com");

        // List<String> listeUrl = new ArrayList<String>();
        // listeUrl.add("http://www.terredeson.com/media/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/r/g/rgbamplifierdune_3_1_3_1_9.jpg");
        // ImageDownloader.downloadImg("imgs/test", listeUrl.get(0));

        // Navigation navigation = parsingAccueil();
        // File f = new File("www.terredeson.com/eclairage/commandes-dmx.html");
        // parsingSousCategorie(f);

        // Parser.extractProductUrlFromDisk("www.terredeson.com");
        // parsingSousCategorie("http://www.terredeson.com/eclairage/blocs-de-puissance-dmx.html");
        // System.out.println(navigation.toString());
        // parsingArticlesAspires();
        // Parser.extraireArticlesSite("http://www.terredeson.com", true);
        // File f = new File("www.terredeson.com/accueil.html");
        // Parser.extraireArticlesSite(f);
        return;
    }

    // private static void parsingSousCategorie(File f) {
    // try {
    // ResultatParsingSousCategorie resultatParsingSousCategorie =
    // ParserSousCategorie.parserSousCategorie(
    // f, true);
    // // if (!urlSousPage.isEmpty()) {
    // // ParserSousCategorie.parserSousCategorie(f, false);
    // // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // }

    private static void parsingArticlesAspires() {
        // try {
        // int nombreFichierAAnalyser = 10;
        //
        // long begin = System.currentTimeMillis();
        //
        // System.out.println("Début du scan des " + nombreFichierAAnalyser +
        // " fichiers");
        //
        // File f = new File(
        // "/Users/mykelkel/Documents/Developpement/workspace/Aspiration terre de son");
        //
        // // Récupération de la liste des fichiers
        // System.out.println("Recherche des fichiers...");
        // List<File> listeFichier = FileUtil.getFilesRecursive(f, new
        // SuffixFilter(
        // TypeFilter.FILE, ".html"));
        // System.out.println("Nombre de fichiers trouvés: " +
        // listeFichier.size());
        //
        // int nombreFichierContenantUnArticle = 0;
        //
        // File file;
        // if (nombreFichierAAnalyser > listeFichier.size()) {
        // nombreFichierAAnalyser = listeFichier.size();
        // }
        //
        // Map<String, Article> mapArticle = new HashMap<String, Article>();
        // for (int i = 0; i < nombreFichierAAnalyser; i++) {
        // file = listeFichier.get(i);
        // if (ParserArticle.estFicheProduit(file)) {
        // Article nouvelArticle = ParserArticle.parserInformationArticle(file);
        // System.out.println("Nouvel article trouvé: " +
        // nouvelArticle.toString());
        // String clef = nouvelArticle.genererClef();
        // if (!mapArticle.containsKey(clef)) {
        // mapArticle.put(clef, nouvelArticle);
        // }
        // else {
        // System.out.println("Article " + clef + " déjà présente dans la map");
        // }
        // nombreFichierContenantUnArticle++;
        // }
        // }
        // System.out.println("Nombre de fichiers article: " +
        // nombreFichierContenantUnArticle
        // + " sur " + nombreFichierAAnalyser + " fichiers");
        // long end = System.currentTimeMillis();
        // float time = (end - begin) / 1000f;
        // System.out.println("Temps du scan :" + time + "secondes");
        // final int nombreFichiersEstimes = 100000;
        // float multiplicateur = nombreFichiersEstimes /
        // nombreFichierAAnalyser;
        // System.out.println("Temps estimé du scan pour " +
        // nombreFichiersEstimes + " fichier "
        // + time * multiplicateur + " secondes");
        // }
        // catch (IOException e) {
        // e.printStackTrace();
        // }
    }
}
