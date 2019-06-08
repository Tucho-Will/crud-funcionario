package crud.domain.cadastro.funcionario.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;

/**
 * @author Fagner W. Mateus
 * @since 07/06/2019
 */

@Data
@Entity
@Table(name = "funcionario")
@SequenceGenerator(name = "funcionario_seq", sequenceName = "funcionario_seq")
public class Funcionario {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "routegeneric_seq")
	private long idFuncionario;
	@Column(length = 30)
	private String nome;
	@Column(length = 50)
	private String sobreNome;
	@Column(length = 12)
	private String nis;
	@Column(length = 80)
	private String email;
}
