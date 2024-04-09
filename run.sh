cd la-lexico/ && mvn clean package && cd ..
java -jar compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar\
     "java -jar la-lexico/target/la-lexico-1.0-SNAPSHOT-jar-with-dependencies.jar"\
      gcc ./temp ./casos-de-teste\
       "801258, 800220, 801839" t1