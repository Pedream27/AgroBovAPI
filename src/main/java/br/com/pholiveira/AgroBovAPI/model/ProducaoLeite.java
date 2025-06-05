package br.com.pholiveira.AgroBovAPI.model;


import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

@Entity
@Table(name = "producoes_leite")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProducaoLeite extends RepresentationModel<ProducaoLeite> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    // @JsonBackReference // Usar se houver problemas de serialização circular
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vaca_id", nullable = false)
    private Vaca vaca;

    @Column(nullable = false)
    private LocalDate dataRegistro;

    @Column(nullable = false)
    private Double quantidadeLitros;
}
