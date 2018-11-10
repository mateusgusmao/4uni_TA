package br.edu.ifpe.jpa.jinq;

import java.util.Date;
import java.util.List;

import br.edu.ifpe.jpa.IReportGenerator;
import br.edu.ifpe.jpa.entities.Account;
import br.edu.ifpe.jpa.entities.Client;
import java.util.stream.Collectors;
import java.util.List;

import br.edu.ifpe.jpa.IReportGenerator;
import br.edu.ifpe.jpa.entities.Client;

public class ReportJinq implements IReportGenerator {
	
	static EntityManagerHelper helper = EntityManagerHelper.getInstance();

	@Override
	public List<String> getClientNames(Date initialDate, Date finalDate) {
		return helper.execute(Client.class, streams ->
			streams
		             .where(client -> client.getBirthDate().after(initialDate))
		        .where(client -> client.getBirthDate().before(finalDate))
			.select(Client::getName)
			.sortedBy(client -> client)
			.toList()
		); 
	}
	
	// Retorna valor total em dinheiro que o cliente possui no banco, somandotodas as suas contas
	
	@Override
         public double getClientTotalCash(String email) {

        return helper.execute(Account.class,
                streams
                -> streams
                   .where(account -> account.getClient().getEmail().equals(email))
                   .sumDouble(Account::getBalance)
        );

    }

	
	//Retorna uma lista (ranking) com os e-mails dos clientes que possuem mais dinheiro no banco, somando todas suas contas.
	@Override
    public List<String> getBestClientsEmails(String agency, int rankingSize) {

        List<String> emails = null;
        emails.addAll(
                helper.execute(Account.class,
                        streams
                        -> streams
                           .sortedBy(Account::getBalance)
                           .limit(rankingSize)
                           .where(a -> a.getAgency().equals(agency))
                           .select(a -> a.getClient().getEmail())
                           .sumDouble(Account::getBalance)
                     ));
        return emails;

    }
}
