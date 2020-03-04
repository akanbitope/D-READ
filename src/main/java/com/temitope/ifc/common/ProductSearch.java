package com.temitope.ifc.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.Search;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.MustJunction;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.temitope.model.Category;
import com.temitope.model.Material;
import com.temitope.model.Synonym;

import antlr.StringUtils;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class ProductSearch {
	Tokenizer tokenizer = null;
	private SessionFactory sessionFactory;
	private String MaterialTitle = null;
	private String thickness = null;

	public ProductSearch() {
		sessionFactory = HibernateRegistry.getSessionJavaConfigFactory();
		TokenizerModel model = null;
		try (InputStream modelIn = new FileInputStream("./classifiers/en-token.bin")) {
			model = new TokenizerModel(modelIn);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		tokenizer = new TokenizerME(model);
	}

	public String[] tokenizer(String string) {
		String rx_prop_sec = "(\\((.+)\\))";
		Pattern p = Pattern.compile(rx_prop_sec);// . represents single character
		Matcher m = p.matcher(string);
		if (m.find()) {
			string = m.group();
			string = StringUtils.stripFrontBack(string, "'", "'");
			string = StringUtils.stripFrontBack(string, "\"", "\"");
		}
		String[] tokenizeArray = tokenizer.tokenize(string);
		// System.out.println(Arrays.toString(tokenizeArray));
		// System.out.println("||| "+string +" ||||");
		return tokenizeArray;
	}

	public Material doExecution() {
		// System.out.println(">>>>>>>>>>>>>>>>>START<<<<<<<<<<<<<<<<<<<<<<<");

		String[] tokenArray = tokenizer(getMaterialTitle());
		List<Material> materials = getAllMaterial(tokenArray, getThickness());

		if (materials == null || materials.isEmpty()) {
			return executeOnMaterialNotFound();
		}

		// System.out.println(materials.size());
		if (materials.size() == 1) {
			return checkFieldValidation(materials.get(0));
		} else if (materials.size() > 1) {
			System.out.println("We found more than one material");
			int index = 1;
			for (Material material : materials) {
				System.out.println(index++ + ". Unit Cost of " + material.getTitle() + " with thickness "
						+ material.getThickness() + " is $" + material.getCost());
			}
			System.out.println("Hit enter for 1 or insert your choise");
			boolean flag = true;
			String i = null;
			int value = 0;
			while (flag) {
				try {
					Scanner scan = new Scanner(System.in);
					i = scan.nextLine();
					if ("".equals(i)) {
						value = 1;
					} else {
						value = Integer.valueOf(i);
					}
					if (value <= materials.size()) {
						flag = false;
					}
				} catch (Exception e) {
					System.out.println("Invalid Input !!");
					System.out.println("Hit enter for 1 or insert your choise");
				}
			}
			return checkFieldValidation(materials.get(value - 1));

		}
		return null;
	}

	public List getAllMaterial(String[] tokens, String thickness) {

		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Material.class);
		Disjunction objDisjunction = Restrictions.disjunction();
		for (String token : tokens) {
			// criteria.add(Restrictions.disjunction().add(Restrictions.like("words",
			// token).ignoreCase()));
			objDisjunction.add(Restrictions.like("title", "%" + token + "%").ignoreCase());
		}
		// Attach Disjunction in Criteria
		criteria.add(objDisjunction);
		if (!(null == thickness || "".equals(thickness))) {
			criteria.add(Restrictions.sqlRestriction("ROUND(thickness, 6) = ROUND("+thickness+",6)"));
		}
		List list = criteria.list();
		List<Material> sortedList = null;
		if (list == null || list.size() == 1) {
			sortedList = list;
		} else {
			sortedList = orderMaterialList(list, tokens);
		}
		session.getTransaction().commit();
		session.close();
		return sortedList;
	}

	private List<Material> orderMaterialList(List<Material> list, String[] tokens) {

		Map orderedMaterialMap = new HashMap<Material, Integer>(list.size());
		int count = 0;
		for (Material material : list) {
			count = 0;
			for (String token : tokens) {
				if (material.getTitle().toLowerCase().contains(token.toLowerCase())) {
					count++;
				}
			}
			orderedMaterialMap.put(material, count);
		}

		// List<Material> list1 = new ArrayList<Material>(orderedMaterialMap.keySet());

		List<Map.Entry<Material, Integer>> list2 = new LinkedList<Map.Entry<Material, Integer>>(
				orderedMaterialMap.entrySet());

		Collections.sort(list2, new Comparator<Map.Entry<Material, Integer>>() {
			@Override
			public int compare(Map.Entry<Material, Integer> a1, Map.Entry<Material, Integer> a2) {
				return a2.getValue().compareTo(a1.getValue()); // I tried also comparing to -timesPlayed2 but this
				// doesn't sort properly
			}

		});

		List<Material> sortedlist = new ArrayList<>();
		for (Map.Entry<Material, Integer> entry : list2) {
			sortedlist.add(entry.getKey());
		}
		// LinkedList<Material> sortedMaterialList = new
		// LinkedList<Material>(sortedMap.values());
		return sortedlist;
	}

	public List<Material> cast(List<Object[]> list) {
		List<Material> materials = new LinkedList<Material>();

		for (Object[] obj : list) {
			// throws casting exception
			Material temMaterial = new Material();
			temMaterial.setId((int) obj[0]);
			int catID = (int) obj[1];
			temMaterial.setTitle((String) obj[2]);
			temMaterial.setThickness((Double) obj[3]);
			temMaterial.setCost((Double) obj[4]);
			if ((Boolean) obj[5]) {
				temMaterial.setStatus((byte) 1);
			} else {
				temMaterial.setStatus((byte) 0);
			}
			materials.add(temMaterial);
		}

		return materials;
	}

	public List<String> getAllSynoKeyWords(String[] tokens) {
		List<String> allSynonames = new ArrayList<>();
		List<Synonym> synonyms = getAllSynoEntry(tokens);
		for (Synonym synonym : synonyms) {
			String names = StringUtils.stripFrontBack(synonym.getWords(), "|", "|");
			String[] nameArray = names.split("\\|", 0);
			allSynonames.addAll(Arrays.asList(nameArray));

		}
		if (allSynonames.size() > 0) {
			for (String word : tokens) {
				if (!allSynonames.contains(word)) {
					allSynonames.add(word);
				}
			}
		}

		return allSynonames;
	}

	@SuppressWarnings("deprecation")
	@Transactional
	public List<Synonym> getAllSynoEntry(String[] tokens) {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Synonym.class);
		Disjunction objDisjunction = Restrictions.disjunction();
		for (String token : tokens) {
			// criteria.add(Restrictions.disjunction().add(Restrictions.like("words",
			// token).ignoreCase()));
			objDisjunction.add(Restrictions.like("words", "%" + token + "%").ignoreCase());
		}
		/* Attach Disjunction in Criteria */
		criteria.add(objDisjunction);
		List list = criteria.list();
		session.getTransaction().commit();
		session.close();
		return list;

	}

	public Material getDefault() {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Material.class);
		criteria.add(Restrictions.eq("title", "Default").ignoreCase());
		Material material = (Material) criteria.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return material;

	}

	public Material checkFieldValidation(Material material) {
		if (material.getThickness() != null && material.getCost() != null && material.getThickness() != 0
				&& material.getCost() != 0) {
			return material;
		}
		boolean flag = true;
		double i = 0;
		if (material.getThickness() == null || material.getThickness() == 0) {
			System.out.println("Please enter thickness for " + material.getTitle());
			flag = true;
			i = 0;
			while (flag) {
				try {
					Scanner scan = new Scanner(System.in);
					i = scan.nextDouble();
					flag = false;
				} catch (Exception e) {
					System.out.println("Invalid Input !!");
					System.out.println("Please enter thickness for " + material.getTitle());
				}
			}
			material.setThickness(i);
		}

		if (material.getCost() == null || material.getCost() == 0) {
			// System.out.println(material.getTitle() +" cost not found!");
			System.out.println("Please enter cost for " + material.getTitle());
			flag = true;
			i = 0;
			while (flag) {
				try {
					Scanner scan = new Scanner(System.in);
					i = scan.nextDouble();
					flag = false;
				} catch (Exception e) {
					System.out.println("Invalid Input !!");
					System.out.println("Please enter cost for " + material.getTitle());
				}
			}
			material.setCost(i);
		}
		saveMaterial(material);

		return material;
	}

	public void saveMaterial(Material material) {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Material.class);
		if(material.getId()!=0)
			criteria.add(Restrictions.eq("id", material.getId()));
		session.saveOrUpdate(material);
		session.getTransaction().commit();
		session.close();
	}
	
	public Category getCategoryById(int id) {
		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();
		Criteria criteria = session.createCriteria(Category.class);
		criteria.add(Restrictions.eq("id", id));
		Category category=(Category)criteria.uniqueResult();
		session.getTransaction().commit();
		session.close();
		return category;
	}

	public Material executeOnMaterialNotFound() {
		System.out.println("Unable to find below material:");
		System.out.println("Material Name: " + getMaterialTitle());
		System.out.println("Material thickness: " + getThickness());
		System.out.println("Do you want to use Default material ?");
		System.out.println("Hit enter for 'no' or insert your choise (yes/no)");
		boolean flag = true;
		String i = null;
		while (flag) {
			try {
				Scanner scan = new Scanner(System.in);
				i = scan.nextLine();
				if ("yes".equalsIgnoreCase(i) || "no".equalsIgnoreCase(i) || "".equalsIgnoreCase(i)) {
					flag = false;
				} else {
					System.out.println("Please provide your input yes/no");
				}

			} catch (Exception e) {
				System.out.println("Invalid input !!");
				System.out.println("Do you want to use Default material ?");
				//System.out.println("Hit enter for 'no' or insert your choise (yes/no)");
			}
		}
		if ("yes".equalsIgnoreCase(i)) {
			System.out.println("Use a default material,With values? yes/no");
			while (flag) {
				try {
					Scanner scan = new Scanner(System.in);
					i = scan.nextLine();
					if ("yes".equalsIgnoreCase(i) || "no".equalsIgnoreCase(i)) {
						flag = false;
					}

				} catch (Exception e) {
					System.out.println("Please provide your input yes/no");
				}
			}
			if ("yes".equalsIgnoreCase(i)) {
				ProductSearch p = new ProductSearch();
				return checkFieldValidation(p.getDefault());
			} else if ("no".equalsIgnoreCase(i)) {
			}
		} else if("no".equalsIgnoreCase(i)) {
			System.out.println("Please provide details for material.");
			Material material = new Material();
			try{
				material.setTitle(getMaterialTitle());
				material.setThickness(Double.parseDouble(getThickness()));
				material.setCategory(getCategoryById(3));
				material.setStatus((byte)1);
			}
			catch(Exception e)
			{
				System.out.println("Invalid input !!");
			}
			// no selected
			return checkFieldValidation(material);
		}
		return null;
	}


	public String getMaterialTitle() {
		return MaterialTitle;
	}

	public void setMaterialTitle(String materialTitle) {
		MaterialTitle = materialTitle;
	}

	public String getThickness() {
		return thickness;
	}

	public void setThickness(String thickness) {
		this.thickness = thickness;
	}

}