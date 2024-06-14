package com.mfc.infra.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArqZipUtil {

    Logger logger = LoggerFactory.getLogger(ArqZipUtil.class);

    public final byte[] generarZipDesdeTareas(final List<String> tareas) throws Exception {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream)) {
            String testRootFolderPath = "test/java/";
            ZipEntry folderEntry = new ZipEntry(testRootFolderPath);
            zipOutputStream.putNextEntry(folderEntry);
            tareas.forEach((task) -> {
                try {
                    scanRecursiveTreeOfTasks(testRootFolderPath, task, zipOutputStream);
                } catch (Exception e) {
                    return;
                }
            });

            // Cerrar el archivo Zip
            zipOutputStream.finish();
            /**** escribir este array en un path del servidor a efectos de depuración::**/
            /*String filePath = "C:\\temp\\testSuite.zip";
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.close();
            System.out.println("Archivo guardado con éxito en el servidor.");*/

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            throw new Exception("Error");
        }
    }

    private void scanRecursiveTreeOfTasks(final String parentDirPath, final String tarea,
                                          final ZipOutputStream zipOutputStream) throws Exception {
        /*if (tarea.getLevel().contentEquals(Tarea.TIPO_TESTCLASS)) { //condicion de parada
            ZipEntry zipEntry = new ZipEntry(parentDirPath + tarea.getTest());
            zipOutputStream.putNextEntry(zipEntry); // Agregar la entrada al archivo Zip
            zipOutputStream.write(tarea.getContents().getBytes()); // Agregar el contenido de la tarea al archivo Zip
            zipOutputStream.closeEntry(); // Cerrar la entrada actual
        } else if ((tarea.getLevel().contentEquals(Tarea.TIPO_PACKAGE)
                || tarea.getLevel().contentEquals(Tarea.TIPO_FOLDER)) && !tarea.getChildren().isEmpty()) {
            // hacer llamada recursiva
            String folderPath = parentDirPath + tarea.getFolderName() + "/";
            ZipEntry zipEntry = new ZipEntry(folderPath); // Define el nombre de la carpeta
            zipOutputStream.putNextEntry(zipEntry); // Agregar la entrada al archivo Zip
            tarea.getChildren().forEach((child) -> {
                try {
                    if (child != null) {
                        scanRecursiveTreeOfTasks(folderPath, child, zipOutputStream);
                    }
                } catch (Exception e) {
                    return;
                }
            });
        }*/
        ZipEntry zipEntry = new ZipEntry(parentDirPath + tarea);
        zipOutputStream.putNextEntry(zipEntry); // Agregar la entrada al archivo Zip
        zipOutputStream.write("prueba...".getBytes()); // Agregar el contenido de la tarea al archivo Zip
        zipOutputStream.closeEntry(); // Cerrar la entrada actual
    }
}
