package com.liferay.fixpack.demo.portlet;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.ContentTypes;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Dictionary;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;


@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.display-name=Fix pack demo portlet",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class ModuleFixpackDemoPortlet extends MVCPortlet {

	@Activate
	public void start(BundleContext bc){
		System.out.println(this.getClass().getCanonicalName() + " started");
		_bc = bc;
	}

	private String _getExportedUtilPackage(){
		for(Bundle bundle : _bc.getBundles()) {
			if (bundle.getSymbolicName().equals("com.liferay.dynamic.data.mapping.api")) {
				Dictionary<String, String> dict = bundle.getHeaders();
				String exported = dict.get("Export-Package").toString();
				
				String[] exportedArray = exported.split("\\s*,\\s*");
				for(String exp : exportedArray) {
					if(exp.startsWith("com.liferay.dynamic.data.mapping.util")) {
						String [] data = exp.split("\\s*;\\s*");
						return data[1];
					}
				}
			}
		}
		return "NOPE";
	}
	
	private String _getImportedUtilPackage(){
		for(Bundle bundle : _bc.getBundles()) {
			if (bundle.getSymbolicName().equals("module.fixpack.demo")) {
				Dictionary<String, String> dict = bundle.getHeaders();
				String imported = dict.get("Import-Package").toString();
				
				String[] importedArray = imported.split("\\s*;\\s*");
				
				for(int i = 0; i < importedArray.length; i++) {
					if(importedArray[i].contains("com.liferay.dynamic.data.mapping.util")) {
						return importedArray[i+1];
					}
				}
				/*
				for(String imp : importedArray) {
					if(imp.startsWith("com.liferay.dynamic.data.mapping.util")) {
						String [] data = imp.split("\\s*;\\s*");
						//return data[1];
						return imp;
					}
				}*/
			}
		}
		return "NOPE";
	}

	private List<User> _listUsers(String className){
		if(_uls == null) {
			System.out.println("Null reference");
			return Collections.emptyList();
		}
		else {
			List<User> userList = _uls.getUsers(-1, -1);

			return userList;
		}
	}

	@Override
	public void doView(
			RenderRequest renderRequest, RenderResponse renderResponse)
		throws IOException, PortletException {
		
		renderResponse.setContentType(ContentTypes.TEXT_HTML_UTF8);

		PrintWriter printWriter = renderResponse.getWriter();

		StringBuilder sb = new StringBuilder();
		sb.append("Declared version in manifest for com.liferay.dynamic.data.mapping.util: ");
		sb.append(_getImportedUtilPackage());
		sb.append("\n");
		sb.append("The portal provides ");
		sb.append(_getExportedUtilPackage());
		sb.append("\n");

		/*
		for (User user : _listUsers(this.getClass().getName())) {
			sb.append(user.getFullName());
			sb.append("\n");
		}
		*/

		printWriter.print(sb.toString());
	}

	@Reference
	private UserLocalService _uls;
	private BundleContext _bc;
}