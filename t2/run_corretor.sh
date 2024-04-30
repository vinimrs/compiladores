cd la-sintatico/ && mvn clean package && cd ../../corretor/
java -jar compiladores-corretor-automatico-1.0-SNAPSHOT-jar-with-dependencies.jar\
     "java -jar ../t2/la-sintatico/target/la-sintatico-1.0-SNAPSHOT-jar-with-dependencies.jar"\
      gcc ./temp ./casos-de-teste\
       "801258, 800220, 801839" t2