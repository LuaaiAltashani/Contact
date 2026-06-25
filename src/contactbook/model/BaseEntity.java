package contactbook.model;

//كلاس مجرد يحتوي على حقل id مشترك 
public abstract class BaseEntity {
    private Long id;
//حقل id من نوع Long وعمليات getter/setter
    public BaseEntity() {
    }

    public BaseEntity(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

