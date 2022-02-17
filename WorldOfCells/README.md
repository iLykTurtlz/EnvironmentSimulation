# WorldOfCells

Un projet d'Alexandre Capy et Tristan Charpentier.

Pour travailler sur le repertoire :

```bash
git clone https://github.com/hashFactory/WorldOfCells.git
cd WorldOfCells
```

Pour compiler :

```bash
javac -classpath "./jogl/gluegen2-rt.jar:./jogl/jogl2.jar:." applications/simpleworld/*.java
```

Et tourner :

```bash
export _JAVA_OPTIONS="-Djogl.disable.openglcore"
java -classpath "./jogl/gluegen2-rt.jar:./jogl/jogl2.jar:." applications/simpleworld/MyEcosystem 
```


-----

# Old readme

A template java projet for creating your own 3D environment with agents and cellular automata

Support de code pour l'UE projet du L2 Sorbonne Universite (LU2IN013)
contact: nicolas.bredeche(at)sorbonne-universite.fr

# DEPENDANCES

- Java JDK
- JOGL

# INSTALLATION (Ubuntu)

1. installer la version developpement d'openJDK (exemple avec openjdk ver.8): 

_sudo apt-get install openjdk-8-jdk_

2. installer Jogl: 

_sudo apt-get install libjogl2-java_

3. compiler depuis le répertoire WorldOfCells

_javac -classpath "/usr/share/java/gluegen2-rt.jar:/usr/share/java/jogl2.jar:." applications/simpleworld/*.java_

4. executer

_java -classpath "/usr/share/java/gluegen2-rt.jar:/usr/share/java/jogl2.jar:." applications/simpleworld/MyEcosystem_


# PRISE EN MAIN

- lancer _applications.simpleworld.MyEcosystem_
- pendant l'execution, appuyer sur "h" pour afficher l'aide dans la console
- etudier les codes source du package _applications.simpleworld_

Ressources:
- les classes _World_ et _WorldOfTrees_ contiennent l'essentiel des elements pour creer votre monde
- le package _objects_ contient la definition de quelques objets presents dans l'environnement
- la classe _PerlinNoiseLandscapeGenerator_ est a ecrire

# HISTORIQUE

- 2013-00-00: premiere version mise a disposition des etudiants (L2, UE "projet", UPMC / SU)
- 2020-02-13: mise a jour, correction bug arbre, ajout tuto compilation en ligne de commande
- 2020-02-20: mise a jour tutorial Eclipse
- 2021-02-02: mise a jour avec la derniere version de JOGL + upload sur github

# AUTRES INFORMATIONS

- pour verifier votre version de java et javac: _update-alternatives --config java_ (ou _javac_)
- Probleme possible sur certaines machines: les arbres ne s'affichent pas sur certaines machines
	<p>Solution: dans _src/Objects/Tree.java_, il y a 8 lignes commençant par _gl.glVertex3f(...)_. Il suffit d'inverser les lignes paires et impaires (1 et 2, 3 et 4, etc.)</p>
- utilisation sur une machine virtuelle: l'option 3D de la VM doit être activée (erreur "_VMware: No 3D enabled_").
- OpenJDK: https://openjdk.java.net/install/
- package Ubuntu JOGL: https://packages.ubuntu.com/search?suite=default&section=all&arch=any&keywords=libjogl2-java&searchon=names
