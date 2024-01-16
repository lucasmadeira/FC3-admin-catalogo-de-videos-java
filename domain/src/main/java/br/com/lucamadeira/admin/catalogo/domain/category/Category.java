package br.com.lucamadeira.admin.catalogo.domain.category;

import br.com.lucamadeira.admin.catalogo.domain.AggregateRoot;
import br.com.lucamadeira.admin.catalogo.domain.validation.ValidationHandler;

import java.time.Instant;


public class Category extends AggregateRoot<CategoryID>  implements Cloneable  {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID anId,
                     final String aName,
                     final String aDescription,
                     final boolean isActive,
                     final Instant aCreationDate,
                     final Instant aUpdateDate,
                     final Instant aDeletedDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.deletedAt = aDeletedDate;
    }

    public static Category newCategory(final String aName, String aDescription, boolean aIsActive){
        final var id = CategoryID.unique();
        final var now = Instant.now();
        final var deletedDate = aIsActive ? null : now;
        return new Category(id,aName,aDescription,aIsActive,now,now,deletedDate);
    }
    public static Category with(
            final CategoryID anId,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                anId,
                name,
                description,
                active,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category with(final Category aCategory) {
        return with(
                aCategory.getId(),
                aCategory.name,
                aCategory.description,
                aCategory.isActive(),
                aCategory.createdAt,
                aCategory.updatedAt,
                aCategory.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate(){
        this.deletedAt = null;
        this.active = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate(){
       if(getDeletedAt() == null){
           this.deletedAt = Instant.now();
       }
       this.active = false;
       this.updatedAt = Instant.now();
       return this;
    }

    public Category update(
            final String aName,
            final String aDescription,
            final boolean isActive){
        if(isActive){
            activate();
        }else{
            deactivate();
        }
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;

    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
