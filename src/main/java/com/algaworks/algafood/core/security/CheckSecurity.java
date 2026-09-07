package com.algaworks.algafood.core.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

public @interface CheckSecurity {

	@interface Cozinhas {

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and hasAuthority('EDITAR_COZINHAS')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeEditar {
		}

		@PreAuthorize("@algaSecurity.podeConsultarCozinhas()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}

	@interface Restaurantes {

		@PreAuthorize("@algaSecurity.podeGerenciarCadastroRestaurantes()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeGerenciarCadastro {
		}

		@PreAuthorize("@algaSecurity.podeGerenciarFuncionamentoRestaurantes(#restauranteId)")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeGerenciarFuncionamento {
		}

		@PreAuthorize("@algaSecurity.podeConsultarRestaurantes()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}

	@interface Pedidos {

		@PreAuthorize("@algaSecurity.temEscopoEscrita()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeCriar {
		}

		@PreAuthorize("@algaSecurity.podePesquisarPedidos(#pedidoFilter.clienteId, #pedidoFilter.restauranteId)")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodePesquisar {
		}

		@PostAuthorize("@algaSecurity.temEscopoLeitura() and " + "(hasAuthority('CONSULTAR_PEDIDOS') "
				+ "or returnObject.body.cliente.id == @algaSecurity.usuarioId "
				+ "or @algaSecurity.gerenciaRestaurante(returnObject.body.restaurante.id))")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeBuscar {
		}

		@PreAuthorize("@algaSecurity.podeGerenciarPedidos(#codigoPedido)")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeGerenciarPedidos {
		}
	}

	@interface FormasPagamento {

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and hasAuthority('EDITAR_FORMAS_PAGAMENTO')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeEditar {
		}

		@PreAuthorize("@algaSecurity.podeConsultarFormasPagamento()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}

	@interface Cidades {

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and hasAuthority('EDITAR_CIDADES')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeEditar {
		}

		@PreAuthorize("@algaSecurity.podeConsultarCidades()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}

	@interface Estados {

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and hasAuthority('EDITAR_ESTADOS')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeEditar {
		}

		@PreAuthorize("@algaSecurity.podeConsultarEstados()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}

	@interface UsuariosGruposPermissoes {

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and @algaSecurity.usuarioAutenticadoIgual(#usuarioId)")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeAlterarPropriaSenha {
		}

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and " + "(hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES') "
				+ "or @algaSecurity.usuarioAutenticadoIgual(#usuarioId))")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeAlterarUsuario {
		}

		@PreAuthorize("@algaSecurity.temEscopoEscrita() and hasAuthority('EDITAR_USUARIOS_GRUPOS_PERMISSOES')")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeEditar {
		}

		@PreAuthorize("@algaSecurity.podeConsultarUsuariosGruposPermissoes()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}

	@interface Estatisticas {

		@PreAuthorize("@algaSecurity.podeConsultarEstatisticas()")
		@Retention(RetentionPolicy.RUNTIME)
		@Target(ElementType.METHOD)
		@interface PodeConsultar {
		}
	}
}