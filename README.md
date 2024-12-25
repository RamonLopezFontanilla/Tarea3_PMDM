# Introducción:
Nuestra aplicación se encarga de **almacenar una serie de elementos** (pokemones capturados) **a elegir de un listado** (pokedex), para poder **consultarlos o eliminarlos, si está permitido**, en cualquier momento que usemos de nuevo la aplicación. Dicho uso nos **exigirá estar autenticados** permitiendo mantener la sesión abierta o forzar el cierre de la misma. Y como última característica, comentar que podemos llevar a cabo el uso de ella en **castellano o inglés**, manteniendo dicha elección en nuestras proferencias para sucesivos usos.   

# Características principales:
Para **la autenticación**, nos permite usar usuario/contraseña, o autenticación de Gmail. Si no cerramos sesión, dicha autenticación no será necesaria hacerla en sucesivos usos de la aplicación ya que será recordada.<br>
El listado de pokemones disponibles para ser capturados aparece en **la Pokédex** en forma de cuadrículas mostrando el nombre e imagen del mismo, y aparecerá con fondo gris si ya está capturado.<br>
La **lista de pokemones capturados** muestra algunos datos complememtarios de cada uno de ellos (número, peso y altura), y nos permite seleccionarlos para acceder al resto de detalles del mismo (incluye los tipos). En esta última pantalla de detalles podremos eliminar el pokemon de nuestras capturas, si está activada la aopción de eliminación.<br>
En **los ajustes** de la aplicación, aparece una primera opción para permitir o restingir la opción de **eliminar capturas**. También podemos cambiar el **idioma** de nuestra aplicación. Estas dos características son almacenadas en las preferencias para ser recordadas en futuros usos.<br>
Podemos ver tambíen la **información sobre la aplicación** (desarrollador y versión), y por último podemos forzar el **cierre de la sesión**.

# Tecnologías utilizadas:
La autenticación ha sido implementada usando el servicio de **Authentication de Firebase**, permitiendo dos proveedores: **email/contraseña, y Gmail**.<br> 
Para extraer la lista de todos los pokemones disponibles para ser capturados, se ha usado la **API disponible** con dicha información, y ha sido consultada **usando la librería retrofit**, extrayendo el nombre y la imagen de ellos y **mostrados en un RecyclerView** con aspecto de con aspecto de **GridLayout**.<br>
Al seleccionar un pokemon para ser capturado, volvemos a usar **retrofit** para obtener datos complementarios del mismo (número, peso, atura y tipos), y almacenamos dicho objeto pokemon con todas estas características usando **Firestore Database, de Firebase**.  
Los pokemones capturados también se muestran dentro de un **RecyclerView**, esta vez con aspecto de **LinearLayout** vertical.<br>
Las características que queremos hacer persisitentes como son el permiso para eliminar pokemones y el idioma de la App, se han almacenado en las **SharedPreferences**.

# Instrucciones de uso:
Para clonar el repositorio:<br>
  1.- Suponemos previamente **Android Studio** en nuestro equipo.<br>
  2.- Abrimos el **menú Git** y seleccionamos la **opción Clone**.<br>
  3.- Confirmamos que en el desplegablde de versión de control aparece la **opción Git**.<br>
  4.- Escribimos dentro de URL: **https://github.com/RamonLopezFontanilla/Tarea3_PMDM.git**.<br>
  5.- Confirmamos o modificamos el directorio local donde se clonará el proyecto.<br>

Para instalar dependencias en la aplicación:<br>
  1.- Abrimos el fichero **build.gradle.kts(Module:app)** de nuestro proyecto.<br>
  2.- Nos situamos en el apartado de declacaión de las dependencias: **dependencies {**<br>
  3.- Añadimos una nueva línea comenzando con **implementation("** y seguida pon la dirección de lalibrería entrecomilladas.<br>
  4.- Como siempre que modificamos el archivo Gradle del proyecto o de la app terminamos **sincronizando** para fijar los cambios.<br>
  
# Conclusiones del desarrollador: Reflexiona sobre el proceso de desarrollo y cualquier desafío o aprendizaje obtenido durante el proyecto.
Considero que se trata de una aplicación muy completa que aglutina varias tecnologías interesantes que se encuentran disponibles como son las APIs y Firebase, y que hemos aprendido a implementar, junto con otras como son el ReclyclerView y el SharedPreferences que ya habiamos usado.<br>
El desarrollo para mi ha sido como una montaña rusa a la hora de la implementarión, con momentos de dominio y otros de "atascos duraderos", como el encontrado para cambiar y mantener el color gris de fondo de los cardViews seleccionados del Pokédex, o al extraer los tipos de un pokemon.

# Capturas de pantalla:

![image](https://github.com/user-attachments/assets/9111e59d-e004-488e-b167-70c2f8b30437)
![image](https://github.com/user-attachments/assets/234af75e-7775-41a5-a500-3407c3779b43)
![image](https://github.com/user-attachments/assets/c2776b35-bdee-470f-9b55-ff1687e2f556)
![image](https://github.com/user-attachments/assets/6778ca7a-cdf1-43ed-b8e7-8a8017267f4b)
![image](https://github.com/user-attachments/assets/8d753865-0996-4869-a3f7-63f8c881d1e4)

# Video demostración:
Se puede ver una demostración del funcionamiento en el siguiente enlace:
https://drive.google.com/file/d/1FMP3RWE_Ulo3cmYLOMZiA8SNeDETCJao/view?usp=drive_link
