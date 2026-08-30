package models;
import java.util.List;

public class Pet {

        private int id;
        private String name;
        private Category category;
        private List<String> photoUrls;
        private List<Tag> tags;
        private String status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public List<String> getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(List<String> photoUrls) {
        this.photoUrls = photoUrls;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static class Category{
            public int id;
            public String name;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
         public static class Tag{
             public int id;
             public String name;

             public int getId() {
                 return id;
             }

             public void setId(int id) {
                 this.id = id;
             }

             public String getName() {
                 return name;
             }

             public void setName(String name) {
                 this.name = name;
             }
         }

}
