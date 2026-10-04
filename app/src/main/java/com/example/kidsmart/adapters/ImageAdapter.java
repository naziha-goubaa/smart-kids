package com.example.kidsmart.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.kidsmart.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ImageAdapter
 * ------------
 * Cet adaptateur sert à gérer l'affichage des cartes dans un jeu de mémoire.
 * Il affiche soit le dos de la carte, soit l'image réelle si la carte est retournée.
 * Il stocke l'état (retournée / non retournée) de chaque carte et met à jour l'affichage.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> images;               // Liste des images du jeu (identifiants drawable)
    private LayoutInflater inflater;            // Sert à créer les vues
    private Map<Integer, Boolean> cardFlipped = new HashMap<>(); // Stocke si une carte est retournée

    // Constructeur : récupère le contexte et la liste des images
    public ImageAdapter(Context ctx, List<Integer> imgs) {
        this.context = ctx;
        this.images = imgs;
        this.inflater = LayoutInflater.from(ctx);
    }

    // Nombre total d'éléments (cartes)
    @Override
    public int getCount() {
        return images.size();
    }

    // Retourne l'image d'une carte spécifique
    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    // Identifiant unique d'une carte (son index)
    @Override
    public long getItemId(int i) {
        return i;
    }

    // Crée ou réutilise la vue de la carte et définit ce qu’elle doit afficher (dos ou image)
    @Override
    public View getView(int pos, View convertView, ViewGroup parent) {

        // Réutilise une vue existante si possible (optimisation)
        View view = convertView != null ? convertView : inflater.inflate(
                R.layout.item_memory_card, parent, false);

        ImageView img = view.findViewById(R.id.cardImage);

        // Si la carte est marquée comme retournée → afficher l'image réelle
        if (cardFlipped.containsKey(pos) && cardFlipped.get(pos)) {
            img.setImageResource(images.get(pos));
        }
        // Sinon → afficher le dos de la carte
        else {
            img.setImageResource(R.drawable.card_back);
        }

        return view;
    }

    // Marque une carte comme retournée et rafraîchit l'affichage
    public void flip(int pos) {
        cardFlipped.put(pos, true);
        notifyDataSetChanged();
    }

    // Marque une carte comme non retournée
    public void flipBack(int pos) {
        cardFlipped.put(pos, false);
        notifyDataSetChanged();
    }

    // Vérifie si une carte est retournée ou non
    public boolean isFlipped(int pos) {
        return cardFlipped.containsKey(pos) && cardFlipped.get(pos);
    }
}
