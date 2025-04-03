package com.seplag.employee_manager.domain.service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.seplag.employee_manager.application.io.ServidorEfetivoRequest;
import com.seplag.employee_manager.application.io.ServidorEfetivoResponse;
import com.seplag.employee_manager.domain.entity.Cidade;
import com.seplag.employee_manager.domain.entity.Endereco;
import com.seplag.employee_manager.domain.entity.FotoPessoa;
import com.seplag.employee_manager.domain.entity.Lotacao;
import com.seplag.employee_manager.domain.entity.PessoaEndereco;
import com.seplag.employee_manager.domain.entity.ServidorEfetivo;
import com.seplag.employee_manager.domain.entity.Unidade;
import com.seplag.employee_manager.infrastructure.pesistence.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class ServidorEfetivoService {

    private final ServidorEfetivoRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final LotacaoRepository lotacaoRepository;
    private final CidadeRepository cidadeRepository;
    private final PessoaEnderecoRepository pessoaEnderecoRepository;
    private final UnidadeRepository unidadeRepository;
    private final MinioService minioService;
    private final FotoPessoaRepository fotoPessoaRepository;
    // @Transactional
    // public ServidorEfetivo create(ServidorEfetivoRequest request) {
    //     Cidade cidade = new Cidade(null, request.cidade(), request.uf());

    //     Cidade cidadeSalva = cidadeRepository.save(cidade);

    //     Endereco endereco = new Endereco(null, request.tipoLogradouro(), request.logradouro(), request.numero(),
    //         request.bairro(), cidadeSalva, null);

    //     Endereco enderecoSalvo = enderecoRepository.save(endereco);       

    //     ServidorEfetivo servidor = new ServidorEfetivo();

    //     servidor.setNome(request.nome());
    //     servidor.setDataNascimento(request.dataNascimento());
    //     servidor.setSexo(request.sexo());
    //     servidor.setMae(request.mae());
    //     servidor.setPai(request.pai());
    //     servidor.setMatricula(request.matricula());
    //     ServidorEfetivo servidorSalvo = repository.save(servidor);

    //     PessoaEndereco pessoaEndereco = new PessoaEndereco(null, servidorSalvo, enderecoSalvo);
    //     pessoaEnderecoRepository.save(pessoaEndereco);
        
    //     servidorSalvo.setPessoaEnderecos(new ArrayList<>(List.of(pessoaEndereco)));

    //     Unidade unidadeSalva = unidadeRepository.findById(request.unidadeId()).orElseThrow(() -> new EntityNotFoundException("Unidade nao encontrada!"));


    //     Lotacao lotacao = new Lotacao(null, servidorSalvo, unidadeSalva, request.dataLotacao(), null, request.portaria());

    //     Lotacao lotacaoSalva = lotacaoRepository.save(lotacao);

    //     unidadeSalva.getLotacoes().add(lotacaoSalva);
    //     unidadeRepository.save(unidadeSalva);

    //     return repository.save(servidorSalvo);
    // }

    @Transactional
    public ServidorEfetivoResponse create(ServidorEfetivoRequest request) {
        Cidade cidadeSalva = salvarCidade(request);
        Endereco enderecoSalvo = salvarEndereco(request, cidadeSalva);
        ServidorEfetivo servidorSalvo = salvarServidor(request);

        associarEndereco(servidorSalvo, enderecoSalvo);
        associarLotacao(servidorSalvo, request);
        List<String> urlsFotos = salvarFotos(servidorSalvo, request.fotos());
        // servidorSalvo.getFotos()
        // ServidorEfetivoResponse response = new ServidorEfetivoResponse(servidorSalvo.getNome(), servidorSalvo.);
        return new ServidorEfetivoResponse(servidorSalvo.getNome(), urlsFotos);
    }

    private Cidade salvarCidade(ServidorEfetivoRequest request) {
        Cidade cidade = new Cidade(null, request.cidade(), request.uf());
        return cidadeRepository.save(cidade);
    }

    private Endereco salvarEndereco(ServidorEfetivoRequest request, Cidade cidade) {
        Endereco endereco = new Endereco(null, request.tipoLogradouro(), request.logradouro(), request.numero(),
            request.bairro(), cidade, null);
        return enderecoRepository.save(endereco);
    }

    private ServidorEfetivo salvarServidor(ServidorEfetivoRequest request) {
        ServidorEfetivo servidor = new ServidorEfetivo();
        servidor.setNome(request.nome());
        servidor.setDataNascimento(request.dataNascimento());
        servidor.setSexo(request.sexo());
        servidor.setMae(request.mae());
        servidor.setPai(request.pai());
        servidor.setMatricula(request.matricula());
        return repository.save(servidor);
    }

    private void associarEndereco(ServidorEfetivo servidor, Endereco endereco) {
        PessoaEndereco pessoaEndereco = new PessoaEndereco(null, servidor, endereco);
        pessoaEnderecoRepository.save(pessoaEndereco);
        servidor.setPessoaEnderecos(new ArrayList<>(List.of(pessoaEndereco)));
    }

    private void associarLotacao(ServidorEfetivo servidor, ServidorEfetivoRequest request) {
        Unidade unidadeSalva = unidadeRepository.findById(request.unidadeId())
            .orElseThrow(() -> new EntityNotFoundException("Unidade nao encontrada!"));
        Lotacao lotacao = new Lotacao(null, servidor, unidadeSalva, request.dataLotacao(), null, request.portaria());
        unidadeSalva.getLotacoes().add(lotacaoRepository.save(lotacao));
        unidadeRepository.save(unidadeSalva);
    }

    private List<String> salvarFotos(ServidorEfetivo servidor, List<MultipartFile> fotos) {
        List<String> urlsFotos = new ArrayList<>();

        if (fotos != null && !fotos.isEmpty()) {
            List<FotoPessoa> fotoPessoas = fotos.stream().map(foto -> {
                try {
                    String fileName = gerarNomeComHash(foto);
                    minioService.uploadFile(foto, fileName);

                    String urlTemporaria = minioService.getFileUrl(fileName);
                    urlsFotos.add(urlTemporaria);
                    return new FotoPessoa(null, servidor, LocalDate.now(), "servidores", fileName);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao salvar foto", e);
                }
            }).collect(Collectors.toList());
            fotoPessoaRepository.saveAll(fotoPessoas);
        }
        return urlsFotos;
    }

    private String gerarNomeComHash(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        String extension = originalName.substring(originalName.lastIndexOf("."));
        
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return uuid + extension;
    }


    public ServidorEfetivo getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new EntityNotFoundException("Servidor Efetivo não encontrado"));
    }
    
}
