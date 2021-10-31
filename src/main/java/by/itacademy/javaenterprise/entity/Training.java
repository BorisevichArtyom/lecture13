package by.itacademy.javaenterprise.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
public class Training {

    private static final long serialVersionUID = 1L;

    private int id;
    private LocalDateTime date;
    private int userID;

}
