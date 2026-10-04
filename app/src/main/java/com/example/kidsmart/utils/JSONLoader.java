package com.example.kidsmart.utils;

import android.content.Context;

import com.example.kidsmart.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * JSONLoader
 * ----------
 * Utilitaire permettant de charger des fichiers JSON situés
 * dans le dossier "assets" et de les transformer :
 *
 *   - en liste de JSONObject (loadArray)
 *   - en liste d'identifiants d'images drawable (loadCategory)
 *
 * Ce module est utilisé pour charger les données des jeux :
 * animaux, fruits, pays, sons, mots, etc.
 */
public class JSONLoader {

    /**
     * Charge un fichier JSON depuis "assets" et retourne un tableau d’objets JSON.
     *
     * @param ctx        Contexte Android
     * @param assetPath  Chemin du fichier dans assets (ex: "animals.json")
     * @return           Liste de JSONObject correspondant au contenu du fichier
     */
    public static List<JSONObject> loadArray(Context ctx, String assetPath) {
        List<JSONObject> out = new ArrayList<>();
        try {
            // Ouvre le fichier depuis assets
            InputStream is = ctx.getAssets().open(assetPath);

            // Lit tout le contenu du fichier en une seule chaîne
            String s = new Scanner(is).useDelimiter("\\A").next();
            is.close();

            // Convertit la chaîne en tableau JSON
            JSONArray a = new JSONArray(s);

            // Ajoute chaque élément à la liste finale
            for (int i = 0; i < a.length(); i++) {
                out.add(a.getJSONObject(i));
            }

        } catch (Exception e) {
            e.printStackTrace(); // Affiche une erreur si JSON mal formé ou fichier introuvable
        }
        return out;
    }

    /**
     * Charge une catégorie (ex: animals.json) et extrait les images associées.
     * Chaque objet JSON contient un champ "image" → nom de ressource drawable.
     *
     * @param ctx           Contexte Android
     * @param categoryName  Nom de la catégorie (ex : "animals", "fruits")
     * @return              Liste des identifiants des images drawable
     */
    public static List<Integer> loadCategory(Context ctx, String categoryName) {
        List<Integer> list = new ArrayList<>();

        try {
            // Ouvre un fichier du type "animals.json"
            InputStream is = ctx.getAssets().open(categoryName + ".json");

            // Lit tout le fichier en mémoire
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            // Transforme les bytes en chaîne de texte UTF-8
            String json = new String(buffer, "UTF-8");

            // Convertit la chaîne en tableau JSON
            JSONArray array = new JSONArray(json);

            // Parcourt chaque objet du JSON
            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);

                // Récupère la valeur du champ "image" (ex: "img_lion")
                String img = o.getString("image");

                // Convertit un nom de drawable en identifiant (R.drawable.img_lion)
                int resId = ctx.getResources().getIdentifier(img, "drawable", ctx.getPackageName());

                list.add(resId);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
