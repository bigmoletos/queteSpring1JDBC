Odyssey · Spring et JDBC
Étape 0 - Contrôleur

Avant toute chose, crée un package com.wildcodeschool.myProjectWithDB.controllers dans ton projet pour y ranger les contrôleurs de ton application. Tu vas communiquer avec la table wizard de ta base de données : crée donc un contrôleur WizardController au sein de ton package :

package com.wildcodeschool.myProjectWithDB.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class WizardController {
    private final static String DB_URL = "jdbc:mysql://localhost:3306/wild_db_quest?serverTimezone=GMT";
    private final static String DB_USER = "harryp";
    private final static String DB_PASSWORD = "leshorcruxescestlavie";
}

Java
Tu noteras que j'ai mis l'annotation @ResponseBody au niveau de la classe : c'est un raccourci qui t'évitera de la mettre sur chaque méthode de ton contrôleur.

La classe déclare aussi des constantes pour l'url, le nom d'utilisateur et le mot de passe pour se connecter à la base de données -- respectivement DB_URL, DB_USER et DB_PASSWORD : modifie-les pour qu'elles contiennent tes propres valeurs ;)

Étape 1 - Route

Maintenant, le but est de relier une requête client (par exemple : récupérer l'ensemble des données d'une table) avec les données présentes dans la base de données. Tu as vu dans les quêtes précédentes que le serveur intercepte une requête client grâce aux routes déclarées avec @RequestMapping. Par défaut, @RequestMapping configure une route pour la méthode GET. Il existe d'autres méthodes que tu verras dans les quêtes suivantes. Pour rendre ton code plus explicite, tu vas maintenant utiliser l'alias @GetMapping lorsque tu veux faire une route "normale" :

@GetMapping("/api/wizards")
List<Wizard> getWizards() {
    // TODO récupération des données (étape 2)
}

Java
Nous reparlerons du type de retour et de la classe Wizard plus tard. Pour l'instant, focus sur l'objectif : lorsque quelqu'un va interroger le serveur sur http://localhost:8080/api/wizards, tu vas vouloir récupérer l'ensemble des sorciers et les renvoyer au client.

Étape 2 - Requête SQL

Grâce à une requête SQL SELECT, tu vas pouvoir récupérer ces informations. C'est donc dans la route que cette requête sera exécutée. Mais pour ça, tu vas d'abord devoir te connecter à ta base de données :

Connection connection = DriverManager.getConnection(
    DB_URL, DB_USER, DB_PASSWORD
);

Java
DB_URL, DB_USER, DB_PASSWORD sont les constantes que tu as déclarées précédemment : assure-toi de les avoir initialisées avec tes informations.

Tu peux maintenant utiliser ta connexion pour préparer une instruction -- statement en anglais -- à exécuter dans MySQL :

PreparedStatement statement = connection.prepareStatement(
    "SELECT * FROM wizard"
);

Java
Enfin, grâce à cette instruction, tu vas pouvoir exécuter ta requête SQL et en obtenir les résultats :

ResultSet resulSet = statement.executeQuery();

Java
Tous ces appels peuvent lever une exception : tu dois donc les mettre dans un bloc try/catch. Les types que tu utilises -- Connection, PreparedStatement et ResultSet -- sont particuliers : ils implémentent java.lang.AutoCloseable. Cela veut dire que tu peux utiliser une instruction try-with-resources. Si ça ne te parle pas, tu trouveras plus d'infos à ce sujet dans les ressources. Voilà à quoi pourrait ressembler ton code :

try(
    Connection connection = DriverManager.getConnection(
        DB_URL, DB_USER, DB_PASSWORD
    );
    PreparedStatement statement = connection.prepareStatement(
        "SELECT * FROM wizard"
    );
    ResultSet resulSet = statement.executeQuery();
) {
    // TODO retourner les données (étape 3)
} catch (SQLException e) {
    // send HttpStatus 500 to the client if something goes wrong
    throw new ResponseStatusException(
        HttpStatus.INTERNAL_SERVER_ERROR, "", e
    );
}

Java
Étape 3 - Envoi des données au client

L'instance de ResultSet que tu as obtenue n'est pas immédiatement "retournable" : ce sont des données brutes issues de la base de données. Avant de pouvoir les retourner comme réponse au client, tu vas devoir les encapsuler dans un objet qui reprend chaque colonne de la table avec laquelle tu as interagi. Tu peux commencer avec l'id et le prénom de ta table wizard :

class Wizard {

    private int id;
    private String firstname;

    public Wizard(int id, String firstname) {
        this.id = id;
        this.firstname = firstname;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }
}

Java
Il te reste maintenant à transférer les informations de tes résultats vers des instances de Wizard. ResultSet te fournit à la fois des outils pour boucler sur les résultats, et des accesseurs pour extraire les informations en fonction de leur type : getInt pour des entiers, getString pour des chaînes de caractères...

List<Wizard> wizards = new ArrayList<Wizard>();

while(resulSet.next()){
    int id = resulSet.getInt("id");
    String firstname = resulSet.getString("firstname");
    wizards.add(new Wizard(id, firstname));
}

return wizards;

Java
Récapitulatif

Si tu t'es perdu en route, pas d'inquiétude : tu trouveras le code complet du contrôleur dans ce gist. Il ne te reste plus qu'à lancer ton serveur -- mvn spring-boot:run -- et à tester si ta route fonctionne. Pour cela, tu vas découvrir un nouvel outil : Postman.

FAQ JDBC
Une énorme ressource : tu y trouveras beaucoup de réponses, certaines à des questions que tu n'avais pas encore imaginées.
try-with-resource
La doc officielle.
https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html
MeasureMeasure
