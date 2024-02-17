package pt.Dealership.Generic.Colors;

import jakarta.persistence.*;
import pt.Dealership.base.models.EntityBase;

@Entity
public class Color extends EntityBase<Color> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String hexadecimal;

    public Color() {
    }

    public Color(String name, String hexadecimal) {
        this.name = name;
        this.hexadecimal = hexadecimal;
    }

    public Color(Long id, String name, String hexadecimal) {
        this.id = id;
        this.name = name;
        this.hexadecimal = hexadecimal;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getHexadecimal() {
        return hexadecimal;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHexadecimal(String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }
}