Projet basé sur WorldOfCells par les étudiants :


     SOK Chanattan   28720887    Resp. environnement
  
     JARSKI Paul     28710621    Resp. agents


Le projet a pour but de recréer un écosystème simplifié dynamique.
Pour cela plusieurs mécanismes sont implémentés : modèle proie-prédateur, automate cellulaire, bruit de perlin...


Compilation du programme : A l'intérieur du dossier WorldOfCells


     javac -classpath "./jogl/gluegen-rt.jar:./jogl/jogl-all.jar:." applications/simpleworld/*.java
     
     OU pour compiler toutes les classes
     
     javac -classpath "./jogl/gluegen-rt.jar:./jogl/jogl-all.jar:." landscapegenerator/*.java objects/*.java cellularautomata/*.java graphics/*.java utils/*.java worlds/*.java applications/simpleworld/*.java


Exécution :
     
     
     java -classpath "./jogl/gluegen-rt.jar:./jogl/jogl-all.jar:." applications/simpleworld/MyEcosystem


Regardez le rapport du projet pour un résumé complet.
