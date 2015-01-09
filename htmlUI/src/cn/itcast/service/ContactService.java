package cn.itcast.service;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.domain.Contact;

public class ContactService {
	/**
	 * 获取联系人
	 * @return
	 */
	public List<Contact> getContacts(){
		List<Contact> contacts = new ArrayList<Contact>();
		contacts.add(new Contact(12, "张明", "13766666666", 13003));
		contacts.add(new Contact(23, "小小", "130066006", 122003));
		contacts.add(new Contact(98, "李小楷", "186768768", 10988787));
		contacts.add(new Contact(76, "赵得", "1565622566", 1666));
		return contacts;
	}
}
