import os
import sys

# --- CONFIGURAÇÕES ---

# Nome do arquivo de saída que será gerado na raiz do projeto
OUTPUT_FILENAME = "estrutura_do_projeto.txt"

# Extensões de arquivo que serão incluídas no TXT
INCLUDE_EXTENSIONS = {
    '.java', '.xml', '.properties', '.yml', '.md', 
    '.sql', '.html', '.css', '.js'
}

# Pastas que serão completamente ignoradas (e todo o seu conteúdo)
EXCLUDE_DIRS = {
    'target', '.git', '.idea', '.vscode', '.mvn', 
    '__pycache__', 'node_modules', '.settings', '.project'
}

# Arquivos específicos a serem ignorados
EXCLUDE_FILES = {
    OUTPUT_FILENAME,  # Ignora o próprio arquivo de saída
    os.path.basename(__file__) # Ignora o próprio script
}

# --- FIM DAS CONFIGURAÇÕES ---

def generate_tree(dir_path, prefix=""):
    """Gera uma representação em árvore da estrutura de diretórios."""
    output_lines = []
    # Usar listdir e isdir para controle fino sobre a recursão
    try:
        entries = sorted(os.listdir(dir_path))
    except FileNotFoundError:
        return []

    # Separa diretórios de arquivos para processar diretórios primeiro
    dirs = [e for e in entries if os.path.isdir(os.path.join(dir_path, e)) and e not in EXCLUDE_DIRS]
    
    for i, entry in enumerate(dirs):
        connector = "└── " if i == len(dirs) - 1 else "├── "
        output_lines.append(f"{prefix}{connector}{entry}/")
        extension = "    " if i == len(dirs) - 1 else "│   "
        output_lines.extend(generate_tree(os.path.join(dir_path, entry), prefix + extension))
        
    return output_lines


def main():
    """Função principal para executar a extração."""
    script_dir = os.path.dirname(os.path.abspath(__file__))
    output_path = os.path.join(script_dir, OUTPUT_FILENAME)

    print(f"Iniciando a extração do projeto em: {script_dir}")
    print(f"O resultado será salvo em: {output_path}")

    try:
        with open(output_path, "w", encoding="utf-8", errors="ignore") as f:
            # 1. Escreve a estrutura de pastas
            f.write("=" * 80 + "\n")
            f.write("ESTRUTURA DE PASTAS\n")
            f.write("=" * 80 + "\n\n")
            f.write(f"{os.path.basename(script_dir)}/\n")
            tree_lines = generate_tree(script_dir)
            for line in tree_lines:
                f.write(line + "\n")
            f.write("\n\n")

            # 2. Percorre o projeto e escreve o conteúdo dos arquivos
            for root, dirs, files in os.walk(script_dir, topdown=True):
                # Otimização: remove as pastas excluídas da lista de diretórios a serem percorridos
                dirs[:] = [d for d in dirs if d not in EXCLUDE_DIRS]
                
                # Ordena os arquivos para uma saída consistente
                files.sort()
                
                for filename in files:
                    if filename in EXCLUDE_FILES:
                        continue
                        
                    file_ext = os.path.splitext(filename)[1]
                    if file_ext in INCLUDE_EXTENSIONS:
                        file_path = os.path.join(root, filename)
                        relative_path = os.path.relpath(file_path, script_dir).replace("\\", "/")
                        
                        f.write("=" * 80 + "\n")
                        f.write(f"ARQUIVO: {relative_path}\n")
                        f.write("=" * 80 + "\n\n")
                        
                        try:
                            with open(file_path, "r", encoding="utf-8", errors="ignore") as content_file:
                                content = content_file.read()
                                f.write(content)
                        except Exception as e:
                            f.write(f"[ERRO AO LER O ARQUIVO: {e}]\n")
                        
                        f.write("\n\n")

        print("\nExtração concluída com sucesso!")
        print(f"O arquivo '{OUTPUT_FILENAME}' foi criado na raiz do seu projeto.")

    except Exception as e:
        print(f"\nOcorreu um erro durante a extração: {e}", file=sys.stderr)

if __name__ == "__main__":
    main()