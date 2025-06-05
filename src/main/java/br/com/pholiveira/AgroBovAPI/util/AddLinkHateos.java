package br.com.pholiveira.AgroBovAPI.util;

import br.com.pholiveira.AgroBovAPI.controller.ProducaoLeiteController;
import br.com.pholiveira.AgroBovAPI.controller.VacaController;
import br.com.pholiveira.AgroBovAPI.model.ProducaoLeite;
import br.com.pholiveira.AgroBovAPI.model.Vaca;
import com.jayway.jsonpath.JsonPath;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

public class AddLinkHateos {

    public static void addLink(Vaca vaca) {
        Long id = vaca.getId();
        vaca.add(linkTo(methodOn(VacaController.class).getVacaById(id)).withSelfRel());
        vaca.add(linkTo(methodOn(VacaController.class).deleteVaca(id)).withRel("delete_vaca"));
        vaca.add(linkTo(methodOn(VacaController.class).updateVaca(id, vaca)).withRel("update_vaca"));
        // Link para as produções de leite desta vaca
        vaca.add(linkTo(methodOn(ProducaoLeiteController.class).getProducoesPorVaca(id)).withRel("producoes_leite"));
        // Link para a análise de 15 dias desta vaca
        vaca.add(linkTo(methodOn(ProducaoLeiteController.class).analisarProducao15Dias(id)).withRel("analise_producao_15_dias"));
    }

    public static  void addLink ( Long vacaId, ProducaoLeite producaoLeite) {

        producaoLeite.add(linkTo(methodOn(ProducaoLeiteController.class).getProducoesPorVaca(vacaId)).withRel("producoes_da_vaca"));
        producaoLeite.add(linkTo(methodOn(VacaController.class).getVacaById(vacaId)).withRel("vaca"));
        producaoLeite.add(linkTo(methodOn(ProducaoLeiteController.class).analisarProducao15Dias(vacaId)).withRel("analise_producao_15_dias"));
    }
    }

