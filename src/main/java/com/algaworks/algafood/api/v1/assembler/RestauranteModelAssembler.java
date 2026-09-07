package com.algaworks.algafood.api.v1.assembler;

import org.jspecify.annotations.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.controller.RestauranteController;
import com.algaworks.algafood.api.v1.model.RestauranteModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.domain.model.Restaurante;

@Component
public class RestauranteModelAssembler extends RepresentationModelAssemblerSupport<Restaurante, RestauranteModel> {
	private final ModelMapper modelMapper;
	private final AlgaLinks algaLinks;
	private final AlgaSecurity algaSecurity;

	public RestauranteModelAssembler(ModelMapper modelMapper, AlgaLinks algaLinks, AlgaSecurity algaSecurity) {
		super(RestauranteController.class, RestauranteModel.class);
		this.modelMapper = modelMapper;
		this.algaLinks = algaLinks;
		this.algaSecurity = algaSecurity;
	}

	@Override
	@NonNull
	public RestauranteModel toModel(@NonNull Restaurante restaurante) {
		var restauranteModel = createModelWithId(restaurante.getId(), restaurante);
		this.modelMapper.map(restaurante, restauranteModel);
		if (this.algaSecurity.podeConsultarRestaurantes())
			restauranteModel.add(this.algaLinks.linkToRestaurantes("restaurantes"));
		if (this.algaSecurity.podeGerenciarCadastroRestaurantes()) {
			if (restaurante.ativacaoPermitida())
				restauranteModel.add(this.algaLinks.linkToRestauranteAtivacao(restaurante.getId(), "ativar"));
			if (restaurante.inativacaoPermitida())
				restauranteModel.add(this.algaLinks.linkToRestauranteInativacao(restaurante.getId(), "inativar"));
		}
		if (this.algaSecurity.podeGerenciarFuncionamentoRestaurantes(restaurante.getId())) {
			if (restaurante.aberturaPermitida())
				restauranteModel.add(this.algaLinks.linkToRestauranteAbertura(restaurante.getId(), "abrir"));
			if (restaurante.fechamentoPermitido())
				restauranteModel.add(this.algaLinks.linkToRestauranteFechamento(restaurante.getId(), "fechar"));
		}
		if (this.algaSecurity.podeConsultarRestaurantes())
			restauranteModel.add(this.algaLinks.linkToProdutos(restaurante.getId(), "produtos"));
		if (this.algaSecurity.podeConsultarCozinhas())
			restauranteModel.getCozinha().add(this.algaLinks.linkToCozinha(restaurante.getCozinha().getId()));
		if (this.algaSecurity.podeConsultarCidades()) {
			if (restauranteModel.getEndereco() != null && restauranteModel.getEndereco().getCidade() != null) {
				restauranteModel.getEndereco().getCidade()
						.add(this.algaLinks.linkToCidade(restaurante.getEndereco().getCidade().getId()));
			}
		}
		if (this.algaSecurity.podeConsultarRestaurantes())
			restauranteModel
					.add(this.algaLinks.linkToRestauranteFormasPagamento(restaurante.getId(), "formas-pagamento"));
		if (this.algaSecurity.podeGerenciarCadastroRestaurantes())
			restauranteModel.add(this.algaLinks.linkToRestauranteResponsaveis(restaurante.getId(), "responsaveis"));
		return restauranteModel;
	}

	@Override
	@NonNull
	public CollectionModel<RestauranteModel> toCollectionModel(@NonNull Iterable<? extends Restaurante> entities) {
		var collectionModel = super.toCollectionModel(entities);
		if (this.algaSecurity.podeConsultarRestaurantes())
			collectionModel.add(this.algaLinks.linkToRestaurantes());
		return collectionModel;
	}
}
