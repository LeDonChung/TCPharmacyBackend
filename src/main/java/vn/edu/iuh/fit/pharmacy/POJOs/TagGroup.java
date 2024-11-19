package vn.edu.iuh.fit.pharmacy.POJOs;


import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;

@Entity
@Table(name = "tags_groups")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class TagGroup {
    @Id
    @Column(name = "tag_group_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name")
    private String groupName;

    @Column(name = "des", columnDefinition = "TEXT")
    private String des;

    @OneToMany(mappedBy = "tagGroup", cascade = CascadeType.ALL)
    private Collection<Tag> tags;
}
