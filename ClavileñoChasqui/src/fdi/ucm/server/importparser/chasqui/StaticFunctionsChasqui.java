package fdi.ucm.server.importparser.chasqui;

import java.util.ArrayList;
import java.util.List;

import fdi.ucm.server.modelComplete.collection.document.CompleteDocuments;
import fdi.ucm.server.modelComplete.collection.document.CompleteElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteLinkElement;
import fdi.ucm.server.modelComplete.collection.document.CompleteTextElement;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteStructure;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteLinkElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteResourceElementType;
import fdi.ucm.server.modelComplete.collection.grammar.CompleteTextElementType;


/**
 * Clase que define la funcionalidad extra para chasqui mediante funciones staticas.
 * @author Joaquin Gayoso-Cabada
 *
 */
public class StaticFunctionsChasqui {

	public static String getIdov(CompleteDocuments recurso) {
		for (CompleteElement desc : recurso.getDescription()) {
			if (desc instanceof CompleteLinkElement)
				if (((CompleteLinkElement)desc).getValue() instanceof CompleteDocuments)
				{
				CompleteDocuments OV=(CompleteDocuments) ((CompleteLinkElement)desc).getValue();
				return ((CompleteTextElement)(FindDescAtrib(OV.getDescription(), NameConstantsChasqui.IDOVNAME))).getValue();
				}
		}
		return null;
	}



	/**
	 * Funcion que añade el meta a la lista del meta base
	 * @param Padre padre donde añadir
	 * @param Nuevo elemento a añadir
	 * @return el elemento nuevo si se añadio, el gemelo dentro del padre si existia previamente.
	 */
	public static CompleteStructure addMeta(CompleteStructure Padre, CompleteLinkElementType Nuevo) {
		for (CompleteStructure element : Padre.getSons()) {
			if (element instanceof CompleteStructure)
				if ((element instanceof CompleteLinkElementType)&&(((CompleteStructure) element).getName().equals(Nuevo.getName())))
						return (CompleteStructure) element;
		}
		Padre.getSons().add(Nuevo);
		return Nuevo;
	}
	
	/**
	 * Funcion que añade el meta a la lista del meta base
	 * @param Padre padre donde añadir
	 * @param Nuevo elemento a añadir
	 * @return el elemento nuevo si se añadio, el gemelo dentro del padre si existia previamente.
	 */
	public static CompleteStructure addMeta(CompleteStructure Padre, CompleteResourceElementType Nuevo) {
		for (CompleteStructure element : Padre.getSons()) {
			if (element instanceof CompleteStructure)
				if ((element instanceof CompleteResourceElementType)&&(((CompleteStructure) element).getName().equals(Nuevo.getName())))
						return (CompleteStructure) element;
		}
		Padre.getSons().add(Nuevo);
		return Nuevo;
	}

	
	
	
	/**
	 * Funcion que añade el meta a la lista del meta base
	 * @param Padre padre donde añadir
	 * @param Nuevo elemento a añadir
	 * @return el elemento nuevo si se añadio, el gemelo dentro del padre si existia previamente.
	 */
	public static CompleteStructure addMeta(CompleteStructure Padre, CompleteStructure Nuevo) {
		for (CompleteStructure element : Padre.getSons()) {
			if (element instanceof CompleteStructure)
				if (((CompleteStructure) element).getName().equals(Nuevo.getName()))
						return (CompleteStructure) element;
		}
		Padre.getSons().add(Nuevo);
		return Nuevo;
	}



	/**
	 * Funcion que busca una descripcion en una lista de descripciones de un elemento
	 * @param list lista de descripciones
	 * @param text texto a buscar
	 * @return el Meta valu de esa descripcion o null en caso contrario.
	 */
	public static CompleteElement FindDescAtrib(List<CompleteElement> list,
			String text) {
		for (CompleteElement completeElement : list) {
			if(completeElement.getHastype().getName().equals(text))
				return completeElement;
		}
		return null;
	}

	/**
	 * Funcion que borra los acentos y demas problemas.
	 * @param input cadena con posibles acentos
	 * @return cadena sin acentos.
	 */
	public static String remove1(String input) {
	    String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
	    String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
	    String output = input;
	    for (int i=0; i<original.length(); i++) {

	        output = output.replace(original.charAt(i), ascii.charAt(i));
	    }
	    return output;
	}
	
	/**
	 * Añadir un termino al vocabulario
	 * @param term Termino a añadir
	 * @return Termino salida, puede ser el term sin no estaba antes o el que es equivalente E.O.C.
	 */
	public static Boolean addTerm(ArrayList<String> Vocabulario, String term)
	{
	boolean encontrado=false;
	for (String candidato : Vocabulario) {
		if (candidato.equals(term))
			encontrado=true;
	}
	
	if (!encontrado)
		{
		Vocabulario.add(term);
		return true;
		}
	else return false;

	}


	/**
	 * Busca un elemento en una lista de descriptores y un ambito especifico
	 * @param description
	 * @param typeFile
	 * @param Ambito
	 * @return
	 */
	public static CompleteElement FindDescAtribAmbito(
			ArrayList<CompleteElement> description, String typeFile) {
		for (CompleteElement completeElement : description) {
			if(completeElement.getHastype().getName().equals(typeFile))
				return completeElement;
		}
		return null;
	}
	
	/**
	 * Busca un elemento en una lista de descriptores y un ambito especifico
	 * @param description
	 * @param attributo
	 * @param Ambito
	 * @return
	 */
	public static CompleteElement FindDescAtribAmbito(
			ArrayList<CompleteElement> description, CompleteStructure attributo) {
		for (CompleteElement completeElement : description) {
			if(completeElement.getHastype()==attributo)
				return completeElement;
		}
		return null;
	}



	public static CompleteTextElementType clone(CompleteTextElementType values) {
		CompleteTextElementType nueva=new CompleteTextElementType(values.getName(), null, values.getCollectionFather());
		nueva.setMultivalued(values.isMultivalued());
		nueva.setBrowseable(values.isBrowseable());
		nueva.setClassOfIterator(values);
		CompleteStructure hermano = values;
		while (hermano.getBSon()!=null)
			hermano=hermano.getBSon();
		hermano.setBSon(nueva);
		nueva.setBFather(hermano);
		CompleteStructure ClaseadrePre=null;
		for (CompleteStructure elemP : values.getSons()) {
			if (ClaseadrePre!=null&&ClaseadrePre.getClassOfIterator()!=null&&ClaseadrePre!=elemP)
				ClaseadrePre=null;
			ClaseadrePre=cloneP(elemP,nueva,ClaseadrePre);
		}
		return nueva;
	}



	private static CompleteStructure cloneP(CompleteStructure aclonar,
			CompleteStructure padre, CompleteStructure clasePadreHermanos) {
		CompleteTextElementType nueva=new CompleteTextElementType(aclonar.getName(), padre, aclonar.getCollectionFather());
		nueva.setMultivalued(nueva.isMultivalued());
		nueva.setBrowseable(nueva.isBrowseable());
		nueva.setClassOfIterator(clasePadreHermanos);
		padre.getSons().add(nueva);
		CompleteStructure hermano = clasePadreHermanos;
		while (hermano.getBSon()!=null)
			hermano=hermano.getBSon();
		hermano.setBSon(nueva);
		nueva.setBFather(hermano);
		CompleteStructure ClaseadrePre=null;
		for (CompleteStructure elemP2 : aclonar.getSons()) {
			if (ClaseadrePre!=null&&ClaseadrePre.getClassOfIterator()!=null&&ClaseadrePre!=elemP2)
				ClaseadrePre=null;
			cloneP(elemP2,nueva,ClaseadrePre);
		}
		
		return nueva;
		
	}


	
}
