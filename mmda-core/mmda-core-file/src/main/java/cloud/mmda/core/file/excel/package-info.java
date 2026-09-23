/**
 * Excel文件导入导出。
 * <p>
 *     绑定单元格{@link cloud.mmda.core.file.excel.bindings.BindingCell}定义了实体数据和Excel单元格之间的映射关系，
 *     包含两种类型：
 *     <ol>
 *         <li>单一字段绑定单元格{@link cloud.mmda.core.file.excel.bindings.SimpleBindingCell}可双向绑定</li>
 *         <li>表达式绑定单元格{@link cloud.mmda.core.file.excel.bindings.ExpressionBindingCell}是单向的，只能从实体数据写入到Excel单元格</li>
 *     </ol>
 *     两种绑定单元格都可通过其构造器来创建。
 * </p>
 *
 * <hr/>
 * <p>
 *     绑定行{@link cloud.mmda.core.file.excel.bindings.BindingRow}定义了实体数据和Excel行之间的映射关系，它包含
 *     一系列有序的绑定单元格{@link cloud.mmda.core.file.excel.bindings.BindingCell}，实际读写是通过绑定单元格来完成。
 * </p>
 * <p>
 *     在绑定行的基础上，Excel数据表{@link cloud.mmda.core.file.excel.bindings.BindingTable}处理Sheet中一个区域的表格绑定和读写数据。
 *     实体表单若包含子项数据表，可以利用此类实现导入导出功能。
 *     实体列表的导入导出借助此类实现，大数据量时借助分页列表提供者{@link cloud.mmda.core.file.excel.PagedListSupplier}抓取数据。
 *
 * </p>
 * <p>
 *     Excel数据表只跟Sheet互相绑定，而Excel文件读写由{@link cloud.mmda.core.file.excel.ExcelFileReader}和
 *     {@link cloud.mmda.core.file.excel.ExcelFileWriter}提供，它们负责WorkBook和File之间的数据绑定和读写。
 * </p>
 */
package cloud.mmda.core.file.excel;