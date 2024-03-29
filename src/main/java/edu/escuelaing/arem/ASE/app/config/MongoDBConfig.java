package edu.escuelaing.arem.ASE.app.config;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoException;
import com.mongodb.MongoTimeoutException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;

import edu.escuelaing.arem.ASE.app.Ticket;

public class MongoDBConfig {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "logDB";
    private static final String COLLECTION_NAME = "tickets";

    private static MongoClient mongoClient;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection;

    static {
        try {
            // Establecer la conexión con la base de datos MongoDB
            mongoClient = MongoClients.create(CONNECTION_STRING);
            database = mongoClient.getDatabase(DATABASE_NAME);
            collection = database.getCollection(COLLECTION_NAME);
        } catch (MongoTimeoutException e) {
            // Manejo de excepción si hay un tiempo de espera al conectarse a la base de datos
            e.printStackTrace();
            throw new RuntimeException("Error de conexión: no se pudo conectar a la base de datos");
        } catch (MongoException e) {
            // Manejo de excepción general de MongoDB
            e.printStackTrace();
            throw new RuntimeException("Error de MongoDB: " + e.getMessage());
        }
    }


    /**
     * Guarda un ticket en la base de datos MongoDB.
     * @param ticket el ticket a guardar
     */
public static void guardarTicketEnBaseDeDatos(Ticket ticket) {
    try {
        
        Document document = new Document("id", ticket.getId()) // Agregar el ID al documento
                .append("dueñoMarca", ticket.getDueñoMarca())
                .append("url", ticket.getURL())
                .append("estado", ticket.getEstado())
                .append("fechaCreacion", ticket.getFechaCreacion())
                .append("proveedor", ticket.getProveedor())
                .append("correoProveedor", ticket.getCorreoProveedor())
                .append("correoDueñoMarca", ticket.getCorreoDueñoMarca());
                
        collection.insertOne(document);
    } catch (MongoTimeoutException e) {
        e.printStackTrace();
        throw new RuntimeException("Error de conexión: no se pudo conectar a la base de datos");
    } catch (MongoException e) {
        e.printStackTrace();
        throw new RuntimeException("Error de MongoDB: " + e.getMessage());
    }
}


    /**
     * Obtiene los tickets de la base de datos MongoDB.
     * @return una lista de documentos representando los tickets
     */
    public static List<Document> obtenerTicketsDeBaseDeDatos() {
        List<Document> tickets = new ArrayList<>();
        try {
            FindIterable<Document> cursor = collection.find(); // Obtener todos los documentos de la colección
            for (Document doc : cursor) {
                tickets.add(doc); // Agregar cada documento a la lista de tickets
            }
        } catch (Exception e) {
            e.printStackTrace(); // Manejo de excepciones
        }
        return tickets;
    }

    public static void actualizarEstadoDelTicket(String id, String nuevoEstado) {
        try (MongoClient mongoClient = MongoClients.create(CONNECTION_STRING)) {
            MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
            MongoCollection<Document> collection = database.getCollection(COLLECTION_NAME);
            Document filtro = new Document("id", Integer.parseInt(id));
            Document actualizacion = new Document("$set", new Document("estado", nuevoEstado));
            collection.updateOne(filtro, actualizacion);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}