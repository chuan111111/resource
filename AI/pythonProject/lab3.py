import mimetypes as mt
import os

path = "CS305_LAB3(2)\CS305_LAB3"

for filename in os.listdir(path):
    file_path = os.path.join(path, filename)
    mime_type, _ = mt.guess_type(file_path)
    print(f'{filename}: {mime_type}')