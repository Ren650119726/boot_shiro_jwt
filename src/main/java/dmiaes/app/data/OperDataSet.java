package dmiaes.app.data;

import java.util.List;
import java.util.Map;

/**
 * @description 对"List<Map<String, List<Map<String, Object>>>>"的各种操作
 */

@SuppressWarnings("unchecked")
public class OperDataSet {
	private BeanData br = null;
	
	public OperDataSet(BeanData br){
		this.br = br;
	}
	
	
	/**
	 * @description 返回List<Map<String, List<Map<String, Object>>>>中表的数量
	 */
	public Integer getTablesCount() {
		try {
			if (this.br.getListData() == null || this.br.getListData().isEmpty()) {
				return 0;
			} else {
				return ((Map<String, List<Map<String, Object>>>) this.br.getListData().get(0)).size();
			}
		} catch (Exception e) {
			return -1;
		}
	}

	/**
	 * @description 从List<Map<String, List<Map<String, Object>>>>中返回指定的表
	 * @param strTabName：表名
	 */
	public List<Map<String, Object>> getTable(String strTabName) {
		try {
			if (this.br.getListData() == null || this.br.getListData().isEmpty()) {
				return null;
			} else {
				return ((Map<String, List<Map<String, Object>>>) this.br.getListData().get(0)).get(strTabName);
			}
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @description 从List<Map<String, List<Map<String, Object>>>>中返回指定的表 的指定行
	 * @param strTabName：表名
	 * @param iRowIndex：行索引
	 */
	public Map<String, Object> getRow(String strTabName, Integer iRowIndex) {
		try {
			if (this.br.getListData() == null || this.br.getListData().isEmpty()) {
				return null;
			} else {
				return ((Map<String, List<Map<String, Object>>>) this.br.getListData().get(0)).get(strTabName).get(iRowIndex);
			}
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * @description 从List<Map<String, List<Map<String, Object>>>>中返回指定的表 的指定行 的指定列 的数据
	 * @param strTabName：表名
	 * @param iRowIndex：行索引
	 * @param strColName：列名
	 */
	public Object getColumn(String strTabName, Integer iRowIndex, String strColName) {
		try {
			if (this.br.getListData() == null || this.br.getListData().isEmpty()) {
				return null;
			} else {
				return ((Map<String, List<Map<String, Object>>>) this.br.getListData().get(0)).get(strTabName).get(iRowIndex).get(strColName);
			}
		} catch (Exception e) {
			return null;
		}
	}

	
	
	/**
	 * @description 从List<Map<String, List<Map<String, Object>>>>中删除指定的表
	 * @param strTabName：表名
	 */
	public void removeTable(String strTabName) {
		try {
			if (this.br.getListData() != null || !this.br.getListData().isEmpty()) {
				((Map<String, List<Map<String, Object>>>) this.br.getListData().get(0)).remove(strTabName);
			}
		} catch (Exception e) {

		}
	}
	
	/**
	 * @description 从List<Map<String, List<Map<String, Object>>>>中删除指定的表 的指定行
	 * @param strTabName：表名
	 * @param iRowIndex：行索引
	 */
	public void removeRow(String strTabName, Integer iRowIndex) {
		try {
			if (this.br.getListData() != null || !this.br.getListData().isEmpty()) {
				((Map<String, List<Map<String, Object>>>) this.br.getListData().get(0)).get(strTabName).remove(iRowIndex);
			}
		} catch (Exception e) {

		}
	}
}