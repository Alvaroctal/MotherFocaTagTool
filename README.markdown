# MotherFocaTagTool

MotherFocaTagTool (nombre en desarrollo), es una aplicacion basada en java con interfaz grafica que indexa tus peliculas y series en un fichero json.
A continuación se explica el modo en el que se reconoce la sintaxis.

## Peliculas

* Descripcion

	- Se asume que las peliculas estan dentro de un directorio o varios directorios.
	- Un archivo en este directorio se considerá una pelicula suelta.
	- Un directorio en este directorio se considerá una saga, y los archivos de su interior, las peliculas de dicha saga. 

	Veamos el arbol de forma mas simple.

* Sintaxis por defecto

	```
	-Peliculas
		- Thor
			- Thor (2011) [1080] [Dual].mkv
			- Thor 2 - El mundo oscuro (2013) [1080] [Dual].mkv
		- Guerra Mundial Z (2013) [1080] [Dual]
	- ...
	- Otras Peliculas
		- ...
	```

	Anotar solamente que aunque una pelicula petenezca a una saga sus nombre pueden no tener nada que ver, igual otro prefiere usar los directorios para calificar generos y es compatible en ese aspecto.

## Series

* Descripcion

	- Se asume que las series estan dentro de un directorio o varios directorios.
	- Este directorio solo contiene mas directorios (series).
	- Cada directorio de una serie solo contiene directorios (temporadas).
	- Y por ultimo cada temporada solo archivos (capitulos).

	Veamos el arbol de forma mas simple.

* Sintaxis por defecto

	```
	- Series
		- Almost Human
			- Almost Human - Temporada 1 - 1080 [Dual]
				- Almost Human 1x01 - piloto.mkv
				- Almost Human 1x02 - Piel.mkv
				- ...
		- Arrow
			- Arrow - Temporada 1 - 720 [Dual]
				- Arrow 1x01 - piloto.mkv
				- Arrow 1x02 - Honor Thy Father.mkv
				- ...
			- Arrow - Temporada 2 - 1080 [Cast]
				- Arrow 2x01 - City of Heroes.mkv
				- Arrow 2x02 - Identity.mkv
				- ...
			- ...
		- ...
	- ...
	- Otras Series
		- ...
			- ...
				- ...

	```

	Asi como en las peliculas los nombres eran un poco mas flexibles, en series, son totalmente inflexibles.

	```
	- $nombreSerie
		- $nombreSerie - Temporada $numeroTemporada - $definicion [$audio]
			- $nombreSerie - $numeroTemporadax$numeroCapitulo - $nombreCapitulo.extension
	```

	Si una variable no concuerda se tomará como un error tipo, no pertenece a esa serie.

## FTP

* Descripcion

	Tambien tiene una función para, tras crear el indice, subir el fichero json a un drectorio en un servidor ftp. Para ello echaremos un ojo al fichero de configuracion del programa ($home/.configMFTT.json).

* Sintaxis

	```
	{	"ftp":{
			"server": "ip", 
			"user": "ftpUser", 
			"pass": "ftpPass"
		},
		"Peliculas"{
			"ftp": "pathToDir/peliculas.json",
			...
		},
		"Series"{
			"ftp": "pathToDir/series.json",
			...
		}
	}
	```

	Es recomendable crear un usuario de ftp exclusivo para el programa, ya que los datos de acceso no estan cifrados (aun).
	
* Opciones
	- La subida del .json al servidor tras el indexado puede desactivarse desmarcando la opcion en el programa.

## Descarga

* [DropBox](https://dl.dropboxusercontent.com/u/67797304/GitHub/MotherFocaTagTool.jar).

## Licencia

Este software viene sin garantia alguna, y se agradecerán los reportes de bug.