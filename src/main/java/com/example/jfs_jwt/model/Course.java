package com.example.jfs_jwt.model;

public class Course {
    final String id;
    final String name;
    final String objectives;

//    constuctor
   public Course(String id,String name,String objectives){
        this.id=id;
        this.name=name;
        this.objectives=objectives;
    }
//    getters


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getObjectives() {
        return objectives;
    }

    @Override
    public String toString()
    {
        return "Course{" + "id='" + id + '\'' + ", name='" + name + '\'' + ", objectives='" + objectives + '\'' + '}';
    }

}
