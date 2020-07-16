package com.company;

import com.google.gson.Gson;
import com.mongodb.*;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // Global variables
    public static Student[] savingStudentArray = new Student[3];
    public static ArrayList<Student> loadedStudentFromDatabase = new ArrayList<>();

    public static void main(String[] args) {
        // user options
        System.out.print("Enter option 's' to save or 'l' to load: ");
        Scanner input = new Scanner(System.in);
        String enteredData = input.nextLine();

        // creating objects
        Student s1 = new Student("Nazhim",18);
        Student s2 = new Student("Abdul", 17);
        Student s3 = new Student("Fatheeha",19);

        // adding the created objects into an array
        savingStudentArray[0] = s1;
        savingStudentArray[1] = s2;
        savingStudentArray[2] = s3;

        // conditions for the user input
        if(enteredData.equals("s") || enteredData.equals("S")){
            saving();
        }else if(enteredData.equals("l") || enteredData.equals("L")){
            loading();
        }
    }

    // SAVING DATA
    private static void saving() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://nazhimkalam:nazhimkalam123@javamongodb.jkqpa.mongodb.net/myDatabase?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        DB database = mongoClient.getDB("myDatabase");
        DBCollection collection = database.getCollection("myCollection");
        collection.drop();  // deleting all records in the collection

        // since we have 3 objects to save I am going for a loop
        for (Student student : savingStudentArray) {
            Gson gson = new Gson();
            String json = gson.toJson(student);

            BasicDBObject basicDBObject = new BasicDBObject("student", json);
            collection.insert(basicDBObject);
        }
        System.out.println("Successfully saved.....");
    }

    // LOADING DATA
    private static void loading() {
        MongoClientURI uri = new MongoClientURI(
                "mongodb+srv://nazhimkalam:nazhimkalam123@javamongodb.jkqpa.mongodb.net/myDatabase?retryWrites=true&w=majority");

        MongoClient mongoClient = new MongoClient(uri);
        DB database = mongoClient.getDB("myDatabase");
        DBCollection collection = database.getCollection("myCollection");

        DBCursor cursor = collection.find();
        Gson gson = new Gson();

        System.out.println("Loading.....");

        for (DBObject object: cursor) {
            String studentObjectString = (String)object.get("student");   // typecasting from Object to String
            Student student = gson.fromJson(studentObjectString,Student.class);

            loadedStudentFromDatabase.add(student);
        }

        System.out.println("These are the loaded objects:");
        for(int i=0; i<loadedStudentFromDatabase.size(); i++){
            System.out.println("Student "+ (i+1) + " - Name: " + loadedStudentFromDatabase.get(i).getName() + " - Age: " + loadedStudentFromDatabase.get(i).getAge());
        }
    }


}
