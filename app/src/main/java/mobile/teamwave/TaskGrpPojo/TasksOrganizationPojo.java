package mobile.teamwave.TaskGrpPojo;

public class TasksOrganizationPojo {
	int id;
	String name, domain, logo, email, tenant_domain, created_on,
			default_currency, default_currency_symbol;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTenant_domain() {
		return tenant_domain;
	}

	public void setTenant_domain(String tenant_domain) {
		this.tenant_domain = tenant_domain;
	}

	public String getCreated_on() {
		return created_on;
	}

	public void setCreated_on(String created_on) {
		this.created_on = created_on;
	}

	public String getDefault_currency() {
		return default_currency;
	}

	public void setDefault_currency(String default_currency) {
		this.default_currency = default_currency;
	}

	public String getDefault_currency_symbol() {
		return default_currency_symbol;
	}

	public void setDefault_currency_symbol(String default_currency_symbol) {
		this.default_currency_symbol = default_currency_symbol;
	}
}
