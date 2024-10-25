package com.project.CustomerProject;

import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
@Builder
@Getter
@Setter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fullName;
    private String emailAddress;
    private Integer age;
    private String address;

    @OneToOne
    Book book;

    @OneToOne (optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    public boolean isComplete() {
        if(fullName.isEmpty() || emailAddress.isEmpty() || age == null || address.isEmpty()){
            return false;
        }
        return true;
    }
}
