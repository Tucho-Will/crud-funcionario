package crud.domain.cadastro.funcionario.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;

import crud.core.hibernate.HibernateDAO;
import crud.core.hibernate.ResultList;
import crud.domain.cadastro.funcionario.model.Funcionario;

public class FuncionarioDAO {

	public static ResultList getAll(String term, long page, long rowsPerPage) {
		//se for a primeira pagian o resultado sera 
		int firstResult = (int) ((page - 1) * rowsPerPage);
		
		List<Predicate> predicates = new ArrayList<>();
		List<Predicate> predicatesCount = new ArrayList<>();
		Session currentSession = HibernateDAO.getSession();
		try {
			CriteriaBuilder builder = currentSession.getCriteriaBuilder();

			CriteriaQuery<Funcionario> criteriaQuery = builder.createQuery(Funcionario.class);
			Root<Funcionario> fromRPG = criteriaQuery.from(Funcionario.class);
			CriteriaQuery<Funcionario> select = criteriaQuery.select(fromRPG);

			CriteriaQuery<Long> countQuery = builder.createQuery(Long.class);
			Root<Funcionario> fromRPGCount = countQuery.from(Funcionario.class);
			CriteriaQuery<Long> selectCount = countQuery.select(builder.countDistinct(fromRPGCount));

			if (!term.isEmpty()) {
				predicatesCount.addAll(constains(term, builder, fromRPGCount, getPropertiesByTerm(), true));
				predicates.addAll(constains(term, builder, fromRPG, getPropertiesByTerm(), true));
			}

			selectCount.where(predicatesCount.toArray(new Predicate[0]));
			TypedQuery<Long> typedQueryCount = currentSession.createQuery(selectCount);
			Long count = typedQueryCount.getSingleResult();

			if (count <= firstResult) {
				if (page > 1) {
					page = page - 1;
				}
				firstResult = (int) ((page - 1) * rowsPerPage);
			}
			select.where(predicates.toArray(new Predicate[0]));
			select.orderBy(builder.desc(fromRPG.get("idFuncionario")));
			select.distinct(true);

			TypedQuery<Funcionario> typedQuery = currentSession.createQuery(select)//
					.setFirstResult(firstResult)//
					.setMaxResults((int) rowsPerPage);

			return new ResultList(typedQuery.getResultList(), count);
		} finally {
			currentSession.close();
		}
	}

	public static Funcionario getFuncionario(long id) {
		return getFuncionarioByProperty("id", id);
	}

	public static void removerFuncionario(long id) throws Exception {
		Session currentSession = HibernateDAO.getSession();
		try {
			Funcionario ob = currentSession.get(Funcionario.class, id);
			if (ob != null) {
				HibernateDAO.delete(ob);
			}
		} finally {
			currentSession.close();
		}
	}

	private static Funcionario getFuncionarioByProperty(String property, Object value) {
		Session currentSession = HibernateDAO.getSession();
		try {
			CriteriaBuilder builder = currentSession.getCriteriaBuilder();
			CriteriaQuery<Funcionario> query = builder.createQuery(Funcionario.class);
			Root<Funcionario> from = query.from(Funcionario.class);
			TypedQuery<Funcionario> typedQuery = currentSession
					.createQuery(query.select(from).where(builder.equal(from.get(property), value)));
			return typedQuery.getSingleResult();
		} finally {
			currentSession.close();
		}
	}

	private static List<Predicate> constains(String term, CriteriaBuilder builder, Root<?> from,
			List<String> properties, boolean or) {
		List<Predicate> predicates = new ArrayList<>();
		if (or) {
			List<Predicate> predicatesOr = new ArrayList<>();
			for (String prop : properties) {
				predicatesOr.add(constain(term, builder, from, prop));
			}
			predicates.add(builder.or(predicatesOr.toArray(new Predicate[0])));
		} else {
			for (String prop : properties) {
				predicates.add(constain(term, builder, from, prop));
			}
		}
		return predicates;
	}

	//devido limitacao do hibernate 5.3 não ter ilike eu criei um metodo.
	private static Predicate constain(String term, CriteriaBuilder builder, Root<?> from, String property) {
		Predicate like = builder.like(builder.lower(from.get(property)), //
				builder.lower(builder.literal("%" + term + "%")));
		return like;
	}
	
	private static List<String> getPropertiesByTerm() {
		List<String> properties = new ArrayList<>();
		properties.add("nome");
		properties.add("sobreNome");
		properties.add("email");
		properties.add("nis");
		return properties;
	}
}
