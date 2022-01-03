package de.hypercdn.commons.api.properties.checksum;

/**
 * Indicates that the implementing class provides a numeric checksum for its content
 */
public interface ChecksumVerify{

	/**
	 * Calculated checksum value of the data
	 *
	 * @return checksum
	 */
	long getChecksumValue();

}
