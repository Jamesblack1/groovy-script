import groovy.util.*
import groovy.io.FileType
import groovy.xml.*
import java.nio.file.Files
import java.nio.file.Files
import java.nio.file.Paths

println("Creando parse ....")
def parser = new XmlParser()

println("Creando el paquete aplicativo")

// Este closure ejecuta un commando (cmd) y devuelve un hashmap
// con la salida estandar en stdout y la salida de error en stderr
def execCmd = { cmd ->
	def sout = new StringBuilder(), serr = new StringBuilder()
	def proc = cmd.execute()
	proc.consumeProcessOutput(sout, serr)
	proc.waitForOrKill(6 * 1000)
	return [stdout: sout, stderr: serr]
}

// Encontrar los nombres de los archivos que cambiaron entre el 
// branch actual y master.
def out = execCmd("git diff --name-only @~..@  --diff-filter=ACMRTUXB  -- . ':(exclude).git*' ':(exclude)utiles/*' ':(exclude)docs/*' ':(exclude)test/*' ")
def cfiles = out.stdout.split()
println("cfiles ->  " + cfiles)
def pkg = new File("pkg")
def src = new File("src")

println("Creando el directorio del Paquete")

if (pkg.exists()) {
	pkg.deleteDir()
}
pkg.mkdir()
def changes = new StringWriter()

cfiles.each{file -> 
	println("File que cambio -> " + file)
    def ruta = new File(file);
    text = ruta.getText('UTF-8')
    ruta.write (text)
    //ruta.createNewFile()
    
    println("ruta -> " + ruta)
    outProps.put("buildlife/rutaDefinition",ruta)
}
//def dir = ruta;
//println("Dir = " + dir)
