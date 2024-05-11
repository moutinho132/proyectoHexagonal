package com.martzatech.vdhg.crmprojectback.domains.wallet.entities;

import com.martzatech.vdhg.crmprojectback.domains.chat.entities.FileEntity;
import com.martzatech.vdhg.crmprojectback.domains.commons.entities.PersonEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@Entity
@Table(name = "t_person_wallet")
public class WalletEntity implements Serializable {
    @Serial
    private static final long serialVersionUID = -1195883458875961094L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "person_id")
    private PersonEntity person;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @Column(name = "creation_time")
    private LocalDateTime creationTime;
}
