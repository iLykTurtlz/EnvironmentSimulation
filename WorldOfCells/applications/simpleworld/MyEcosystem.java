// ### WORLD OF CELLS ### 
// created by nicolas.bredeche(at)upmc.fr
// date of creation: 2013-1-12

package applications.simpleworld;

import graphics.Landscape;

public class MyEcosystem {
    
	public static void main(String[] args) {

		WorldOfTrees myWorld = new WorldOfTrees();
		
		// parametres:
		// 1: le "monde" (ou sont definis vos automates cellulaires et agents
		// 2: (ca depend de la methode : generation aleatoire ou chargement d'image)
		// 3: l'amplitude de l'altitude (plus la valeur est elevee, plus haute sont les montagnes)
		// 4: la quantite d'eau
		//Landscape myLandscape = new Landscape(myWorld, 128, 128, 0.1, 0.7);
		Landscape myLandscape = new Landscape(myWorld, "media/landscape_paris-200.png", 0.2, 0.42);
		
		Landscape.run(myLandscape);
    }

}
