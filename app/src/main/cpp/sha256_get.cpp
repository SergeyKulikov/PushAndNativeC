#include "sha256.h"

std::string sha256_get(const std::string str)
{
	BYTE hash[SHA256_BLOCK_SIZE];
	SHA256_CTX sha256;
	sha256_init(&sha256);
	sha256_update(&sha256, (BYTE*)str.c_str(), str.size());
	sha256_final(&sha256, hash);

	char outputBuffer[64];
	for (int i=0; i<SHA256_BLOCK_SIZE; i++) {
		sprintf(outputBuffer + (i * 2), "%02x", hash[i]);
	}

	return std::string(outputBuffer);
}
