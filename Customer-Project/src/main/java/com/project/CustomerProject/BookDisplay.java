package com.project.CustomerProject;


import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookDisplay {
    private Long bookId;
    private String title;
    private String genre;
    private Integer pageCount;
    private Long customerId;
    private String customerName;
}
